
var mapObject = L.map("map", { center: [9.988795, 105.786775], zoom: 13 });
L.tileLayer(
    "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    {
        attribution: '&copy; <a href="http://' +
            'www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }
).addTo(mapObject);

var layerObject = L.layerGroup().addTo(mapObject);
let layersEdit = L.featureGroup();
let layersDelete = L.featureGroup();
let layersInsert = L.featureGroup();

var pointStyle = L.icon({
    iconUrl: "/css/images/redIcon.png",
    shadowUrl: "/css/images/marker-shadow.png",
    iconAnchor: [13, 41]  //Giữa đáy ảnh 25, 41 (RClick trên ảnh / Properties)
});
var lineStyle = { color: "red", weight: 2 };
var polygonStyle = { color: "red", fillColor: "yellow", weight: 4 };


var arrayLayerDelete = new Array(); //Mảng này chứa các cartodb_id của đối tượng đã xóa để xóa vào csdl
//var lengthLayerDB;
var arrayInsert = {};   //Mảng này để đánh dấu đối tượng thêm hay xóa
//Mảng checkObject này dùng để đánh dấu Update. Nếu điểm đó đang thao tác thêm sửa xóa thì không hiển thị bản details ngược lại thì có
var checkObject_NotUpdate = {};

var wkx = require("wkx");

// tạo Popup cho hiển thị các form   ///////////////////////////////////////////////////////////
let popupCreate = '<form style="width:300px;" id="form_create">' +
        '<input type="file" id="hinh" class="form-control" multiple> <br/><br/>' +
        '<label for="tenkhudat">Tên khu đất: <input type="text" id="tenkhudat" class="form-control" ></label><br/><br/>' +
        '<label for="diachi">Địa chỉ khu đất: <input type="text" id="diachi" class="form-control" ></label><br/><br/>' +
        '<label for="dientich">Diện tích mặt bằng: <input type="text" id="dientich" class="form-control"></label><br/><br/>' +
        '<label for="giatien">Giá: <input type="text" id="giatien" class="form-control" ></label><br/><br/>' +
        '<label for="mota">Mô tả: <textarea id="mota" class="form-control" cols="30" rows="3"></textarea></label><br/><br/>' +
        '<input type="button" value="Submit" id="submit">' +
        '</form>';

// end tạo Popup cho hiển thị các form ////////////////////////////////////////////////////////////////


////////-------------------------------- Vẽ các hiển thị lên trên Map    --------------------------------------------------------

//Thêm điều khiển vẽ; Icon mặc nhiên trong thư mục css/images để hiển thị và xóa 
var drawnItems = L.featureGroup().addTo(mapObject);
new L.Control.Draw({
    edit: {
        featureGroup: drawnItems
    }
}).addTo(mapObject);

//Tạo nút lệnh Save
var control = L.control({ position: "topright" });
control.onAdd = function (map) {
    var div = L.DomUtil.create("div", "divsave");
    div.innerHTML = '<input type="button" id="save" value="Save" disabled=true>';
    return div;
};
control.addTo(mapObject);

var control = L.control({ position: "topright" });
control.onAdd = function (map) {
    var div = L.DomUtil.create("div", "divsave");
    div.innerHTML = '<select id="combobox_loainhadat" class="form-control"></select>';
    return div;
};
control.addTo(mapObject);

$.getJSON("/LoaiNhaDat/GetAll", function (data) {
    var menu = $("#combobox_loainhadat");
    menu.append("<option>Tất cả</option>");
    $.each(data, function (key, value) {
        menu.append('<option value="' + value.MaLoaiNhaDat + '" >' + value.TenLoaiNhaDat + "</option>");
    });

});

$(document).ready(function () {
    loadMap();
})

function loadMap() {
    //Hiển thị tất cả đối tượng lên bản đồ
    drawnItems.clearLayers();
    layerObject.clearLayers();
    layersDelete.clearLayers();
    layersEdit.clearLayers();
    layersInsert.clearLayers();
    $.getJSON('/NhaDat/AllNhaDat', function (data) {
        lengthLayerDB = data.features.length;		//Gán độ dài layer load lên từ csdl cho lengthLayerDB
        console.log(data);
        L.geoJSON(data, {
            style: function (feature) {
                switch (feature.geometry.type) {
                    case 'LineString': return lineStyle;
                    case 'Polygon': return polygonStyle;
                }
            },
            onEachFeature: function (feature, layer) {
                if (feature.properties && feature.properties.name) {
                    layer.addTo(drawnItems);
                    //Khi load các điểm từ csdl lên thì ta add vào trong featureGroup để có thể cập nhập
                    //Việc add này cũng có thể cho chúng ta thấy đối tượng trên map nếu ta không add vào layerObject
                    layer.bindPopup("" + feature.properties.diachi);
                }
            },
            pointToLayer: function (feature, latlng) {
                return L.marker(latlng, { icon: pointStyle });
            }
        }).addTo(layerObject);		//Add tất cả đối tượng được thêm vào layerObject để phục vụ cho việc so sánh khi truy vấn các đối tượng thêm tiếp theo
        // Việc add vào layerObject này cũng có thể cho chúng ta thấy đối tượng trên đó trên map
    });

    checkChangeMap(saveChange = false);
}

//------------------------------------ Vẽ các hiển thị lên trên Map    ---------------------------------------------------

//++++++++++++++++++++++++++++++++++++      Thao tác thêm sửa xóa      +++++++++++++++++++++++++++++++++++++++++++++++++++

//layer để giữ đối tượng đang vẽ hoặc đang được chọn
var layerFocus = new L.Layer();

// Lấy đối tượng đang được click trên map
drawnItems.on('popupopen', function (e) {
   /* var da = { manhadat: '123213', name: 'adsf' };*/
   /* var da = {};
    da["Tenkhudat"] = "12313";
    da["Diachi"] = "12313";
    da["Giatien"] = "12313";
    da["Dientich"] = "12313";
    da["Mota"] = "12313";
    da["Hinhanh"] = "12313";
    $.ajax({
        async: false,
        type: 'POST',
        data: JSON.stringify(da),
        url: '/NhaDat/InsertNhaDatOnMap',
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: result => {

            console.log(result);
        },
        error: err => {

        }
    });*/

    if (checkObject_NotUpdate["" + e.layer._leaflet_id + ""] != 1) { 
        layerFocus = e.layer;
        $(".leaflet-popup").addClass("form_Object_map");
        $(".leaflet-popup-content-wrapper").addClass("body_form_Object_map");
        $("#div_bando").removeClass("col-lg-12");
        $("#div_bando").addClass("col-lg-9");
        $("#div_bando").addClass("p-0");
        $("#div_Details").css('display', 'block');
        $('.hinh').attr('src',"/css/images/" + e.layer.feature.properties.hinhanh);
        $('.name').html(e.layer.feature.properties.name);
        $('.diachi').html("Địa chỉ: " + e.layer.feature.properties.diachi);
        $('.giatien').html("Giá tiền: " + e.layer.feature.properties.giatien + " tỉ");
        $('.dientich').html("Diện tích: " + e.layer.feature.properties.dientich + " m2");
        $('.mota').html("Mô tả: " + e.layer.feature.properties.mota);
    }
});



//Khi vẽ thì thêm vào lớp drawnItems
mapObject.on("draw:created", function (e) {
    layerFocus = e.layer;
    layerFocus.addTo(drawnItems);
    checkObject_NotUpdate["" + layerFocus._leaflet_id + ""] = 1;
    layerFocus.bindPopup(popupCreate).openPopup();
    arrayInsert[layerFocus._leaflet_id] = 1; //Đánh dấu nó là đối tượng được thêm
    layerFocus.addTo(layersInsert);
});

mapObject.on("draw:edited", function (e) {
    var layers = e.layers;
    layers.eachLayer((layer) => {
        //Kiểm tra xem coi đối tượng đó có tồn tại mã nhà đất không. Nếu không là thêm
        //Nếu phải thì ta vẫn giữ đối tượng này là thêm. Còn đối tượng này có trong csdl rồi thì ta đánh dấu nó là edit
        if (layer.feature.properties.hasOwnProperty('manhadat')) {
            layer.addTo(layersEdit);
        }
    })
    checkChangeMap(saveChange = true);
})

mapObject.on("draw:deleted", function (e) {

    var layers = e.layers;
    layers.eachLayer((layer) => {
        //Kiểm tra đối tượng vừa xóa có cartodb_id không nếu có thì add nó vào arrayLayerDelete. 
        //Nếu không có thì đối tượng delete đó chưa có trong csdl nên việc nó có bị xóa hay không thì không cần để tâm
        if (layer.feature.properties.hasOwnProperty("manhadat")) {
            $("#div_bando").removeClass("col-lg-8");
            $("#div_bando").removeClass("p-0");
            $("#div_bando").addClass("col-lg-12");
            $("#div_Details").css('display', 'none');
            layersEdit.hasLayer(layer) ? layersEdit.removeLayer(layer) : {};
            layersDelete.addLayer(layer);
        } else {
            layersInsert.removeLayer(Layer);
        }
    })
    checkChangeMap(saveChange = true);
})

//++++++++++++++++++++++++++++++++++++      end Thao tác thêm sửa xóa      +++++++++++++++++++++++++++++++++++++++++++++++++++


$("#save").on("click", function () {
    let promise1, promise2, promise3;

    layersInsert.eachLayer((layer) => {
        /* drawnItems.removeLayer(layer);			//Xóa đi đối tượng màu xanh trên bảng đồ*/
        var geometry = layer.toGeoJSON().geometry, geojson = layer.toGeoJSON();

        var wkt = wkx.Geometry._parseGeoJSON(geometry).toWkt();
        var data = {};
        data["Tenkhudat"] = geojson.properties.name;
        data["Diachi"] = geojson.properties.diachi;
        data["Giatien"] = geojson.properties.giatien;
        data["Dientich"] = geojson.properties.dientich;
        data["Mota"] = geojson.properties.mota;
        data["Hinhanh"] = geojson.properties.hinhanh;
        data["wkt"] = wkt;
        promise1 = UpdateToServer('/NhaDat/InsertNhaDatOnMap', data);
    });

    layersEdit.eachLayer((layer) => {
        var geometry = layer.toGeoJSON().geometry, geojson = layer.toGeoJSON();
        var wkt = wkx.Geometry._parseGeoJSON(geometry).toWkt();
        var data = {};
        data["Manhadat"] = geojson.properties.manhadat;
        data["Tenkhudat"] = geojson.properties.name;
        data["Diachi"] = geojson.properties.diachi;
        data["Giatien"] = geojson.properties.giatien;
        data["Dientich"] = geojson.properties.dientich;
        data["Mota"] = geojson.properties.mota;
        data["Hinhanh"] = geojson.properties.hinhanh;
        data["Macsh"] = geojson.properties.macsh;
        data["Malnd"] = geojson.properties.malnd;
        data["wkt"] = wkt;
        promise2 = UpdateToServer('/NhaDat/EditNhaDatOnMap', data);
    });

    layersDelete.eachLayer((layer) => {
        var geometry = layer.toGeoJSON().geometry, geojson = layer.toGeoJSON();
        var wkt = wkx.Geometry._parseGeoJSON(geometry).toWkt();
        var data = {};
        data["Manhadat"] = geojson.properties.manhadat;
        data["Tenkhudat"] = geojson.properties.name;
        data["Diachi"] = geojson.properties.diachi;
        data["Giatien"] = geojson.properties.giatien;
        data["Dientich"] = geojson.properties.dientich;
        data["Mota"] = geojson.properties.mota;
        data["Hinhanh"] = geojson.properties.hinhanh;
        data["Macsh"] = geojson.properties.macsh;
        data["Malnd"] = geojson.properties.malnd;
        data["wkt"] = wkt;
        promise3 = UpdateToServer('/NhaDat/DeleteNhaDatOnMap',data);
    });

    // Process asyn methods (post methods) with Promise:
    Promise.all([promise1, promise2, promise3]).then((re1, re2, re3) => {
        loadMap();
    }).catch((err) => console.log(err));
})

function UpdateToServer(url, data) {
    console.log(data);
    return $.ajax({
        async: false,
        url: '' + url,
        type: 'GET',
        contentType: 'application/json; charset = utf-8',
        data: { data: JSON.stringify(data) },
        dataType: 'json',
        success: result => {
            console.log(result);
        },
        error: err => {

        }
    });
}


$("body").on("click", "#submit", addprops);

//Hàm này dùng để đổi tên của các đối tượng khi submit
function addprops() {
    checkChangeMap(saveChange = true);
        //Nếu layer này mới được thêm thì ta set feature.type, .feature.properties và layer.feature.properties.name bằng tên vừa nhập khi submit
    layerFocus.feature = {};
    console.log(layerFocus.feature);
    layerFocus.feature.type = "Feature";
    layerFocus.feature.properties = {};
    layerFocus.feature.properties.hinhanh = $("#hinh")[0].files[0].name;
    layerFocus.feature.properties.name = $('#tenkhudat').val();
    layerFocus.feature.properties.diachi = $('#diachi').val();
    layerFocus.feature.properties.dientich = $('#dientich').val();
    layerFocus.feature.properties.giatien = $('#giatien').val();
    layerFocus.feature.properties.mota = $('#mota').val();

    console.log(layerFocus.feature.properties);

    layerFocus.bindPopup("" + layerFocus.feature.properties.diachi).closePopup();

    if (layerFocus.feature.properties.hasOwnProperty('manhadat'))
        layersEdit.addLayer(layerFocus);
}

// Bắt sự kiện khi Cập nhật địa điểm
function checkChangeMap(saveChange = false) {
    if (!saveChange) {
        $('#save').attr('disabled', 'true');
        $('#save').animate(
            {
                width: '30px',
                height: '20px',
                opacity: 0.2
            },
            1000
        );
    }
    else {
        $('#save').attr('disabled', false);
        $('#save').animate(
            {
                width: '80px',
                height: '60px',
                opacity: 1
            },
            1000
        );
    }
}
