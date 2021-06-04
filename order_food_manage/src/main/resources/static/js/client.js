function showListMenu(){
    let menu = document.querySelector('.menu_mobile-list');
    if( menu.style.display === "block"){
        $(".menu_mobile-list").slideUp(500,function(){});
    }else{
        $(".menu_mobile-list").slideDown(500,function(){});
    }
}
function showListNav(){
    let menu = document.querySelector('.nav_mobile-list');
    if( menu.style.display === "block"){
        $(".nav_mobile-list").slideUp(500,function(){});
    }else{
        $(".nav_mobile-list").slideDown(500,function(){});
    }
}
// function displayGridView(){
//     document.querySelector(".grid_view").style.display = "flex";
//     document.querySelector(".list_view").style.display = "none";
//     document.getElementById("display_gridview").classList.add("active");
//     document.getElementById("display_listview").classList.remove("active");
// }
// function displayListView(){
//     document.querySelector(".grid_view").style.display = "none";
//     document.querySelector(".list_view").style.display = "flex";
//     document.getElementById("display_gridview").classList.remove("active");
//     document.getElementById("display_listview").classList.add("active");
// }
$(".filter_mobile-title").hover(()=>{
    let filter_mobile = document.querySelector('.filter_mobile-main');
    if( filter_mobile.style.display === "block"){
        filter_mobile.style.display = "none";
    }else{
        filter_mobile.style.display = "block";
    }
})
// Register
$("#confirm_register").click(function(e){
    let userName = $("#userName").val();
    let password = $("#password").val();
    let confirm_password = $("#confirm_password").val();
    let phone = $("#phone").val();
    let vnf_regex = /^(0[2-9][0-9]{8})$/g;
    if( userName.length < 6 ){
        $("#username_error").html("Username 6 characters minimum");
        return false;
    }else $("#username_error").html("");
    if( password.length < 6 ){
        $("#password_error").html("Password 6 characters minimum");
        return false;
    }else $("#password_error").html("");
    if( confirm_password !== password ){
        $("#confirm_password_error").html("Password confirm is incorrect");
        return false;
    }else $("#confirm_password_error").html("");
    if (vnf_regex.test(phone) == false){
        $("#phone_error").html("Phone number is not in the correct format");
        return false;
    }else   $("#phone_error").html("");

    e.preventDefault(); // đây là đường dẫn đến api. Nếu không có đường dẫn này nó sẽ request vào link đang đứng hiện tại
    // Khi data nằm ngoài client thì kiểu dữ dữ liệu của nó sẽ là javascript Object
    const data = {};
    //$('#formSubmit').serializeArray(); dùng để lấy tất cả các name của những field trong form
    let formData = $('#form_register').serializeArray();
    $.each(formData, function(i, v){
        data[""+v.name+""] = v.value;
    });
    addUser(data);
})

//Add user when register
function addUser(data){
    $.ajax({
        url: '/register',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        dataType: 'json',
        success: result=>{
            if( result.userName != null ){
                $('.login').css('position','static')
                showNotificationRegister(result);
            }else{
                $('#username_error').html("Username already exists");

            }
            console.log(result);
        },
        error: result=>{
            alert(result);
        }
    });
}

// View quick and add to cart quick
function displayAction(objectId) {
    document.getElementById("action"+objectId).style.animation = "displayAction 1s forwards";
}

function hiddenAction(objectId){
    document.getElementById("action"+objectId).style.animation = "hiddenAction 1s forwards";
}

function changeImageFocus(image_path){ //change image focus when click image item in view quick
    $('#image_focus').attr("src","/image_upload/"+image_path);
    $('.image_item a').removeClass('active');
    $(this).attr("class","active");
}

function display_viewquick(objectId){
    $.ajax({
        url: '/quick_view_info/'+objectId,
        success: result => {
            $("#quick_view").html(result);
        },
        error: function(result){
            console.log(result);
            alert("error");
        }
    });
    $('#quick_view').css("display","block");
}

function addtocart_quick(objectId){
    $.ajax({
        url: '/cart/add?id='+objectId,
        success: result => {
            let quantityCart = document.querySelector(".cart-popup-count").innerHTML;
            setQuantityInCart(parseInt(quantityCart)+1);
            showNotificationAdded();
        },
        error: function(result){
            console.log(result);
            alert("error");
        }
    });
}

// payment
function changeQuantity(objectId, quantity){
    $.ajax({
        url: '/cart/update?id='+objectId+'&quantity='+quantity,
        type: 'PUT',
        success: result => {
            console.log(result);
            let quatity = 0;
            $(result.listOrderDetail).each(function (i,product){
                if( product.foodDTO.id == objectId ) {
                    $('#subtotal_'+objectId).html(product.subTotal);
                    console.log(product.subTotal);
                }
                quatity += product.quantity;
            })
            setQuantityInCart(quatity);
            $("#total").html(result.total);
            console.log(result.total);
        },
        error: result => {
            console.log(result);
            alert("Error");
        }
    });
}
// delete 1 object trong order
function deleteObject(objectId){
    $.ajax({
        url: '/cart/remove?id='+objectId,
        type: 'DELETE',
        success: result => {
            console.log(result);
            let quatity = 0;
            $(result.listOrderDetail).each(function (i,product){
                if( product.foodDTO.id == objectId ) {
                    $('#subtotal_'+objectId).html(product.subTotal);
                    console.log(product.subTotal);
                }
                quatity += product.quantity;
            })
            setQuantityInCart(quatity);
            $("#total").html(result.total);
            console.log(result.total);
            $("#"+objectId).css("display","none");
            if( quatity == 0 ){
                window.location.assign("/shopping-cart");
            }
        },
        error: result=>{
            console.log(result);
            alert("Không được");
        }
    });
}
// Contact
$('#btnSubmit_contact').click(function (e){
    e.preventDefault(); // đây là đường dẫn đến api. Nếu không có đường dẫn này nó sẽ request vào link đang đứng hiện tại
    // Khi data nằm ngoài client thì kiểu dữ dữ liệu của nó sẽ là javascript Object
    const data = {};
    //$('#formSubmit').serializeArray(); dùng để lấy tất cả các name của những field trong form
    let formData = $('#form_contact').serializeArray();
    $.each(formData, function(i, v){
        data[""+v.name+""] = v.value;
    });
    addContact(data);
})

function addContact(data){
    $.ajax({
        url: '/contact',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        dataType: 'json',
        success: result=>{
            if( result.fullName != null ){
                showNotificationContact();
                console.log(result);
            }else{
                alert("Error")
                console.log(result);
            }
        },
        error: result=>{
            alert(result);
            console.log(result);
        }
    });
}
// Xử lý thêm vào giỏ hàng
function handleAddToCart(id){
    let quantity = document.querySelector('input[type="number"]').value;
    $.ajax({
        url: '/cart/add?id='+id+'&quantity='+quantity,
        type: 'POST',
        contentType: 'json',
        success: result => {
            let quick_view = document.getElementById("quick_view");
            if( quick_view ){
                quick_view.style.display = "none";
            }
            showNotificationAdded();
            let quatity = 0;
            $(result.listOrderDetail).each(function (i,product){
                quatity += product.quantity;
            })
            setQuantityInCart(quatity);
        },
        error: function(result){
            console.log(result);
            alert("error");
        }
    });
}
// cập nhật quantity trên button giỏ hàng
function setQuantityInCart(quantity){
    let quantityCart = document.querySelector('.cart-popup-count');
    quantityCart.innerHTML = quantity;
}

// Thông báo khi thực hiện đăng ký, gửi liên hệ, thêm vào giỏ hàng
function showNotificationAdded(){
    swal({
        title: "Thông báo",
        text: "Đã thêm vào giỏ hàng",
        type: "success",
        cancelButtonClass: "btn-success",
        cancelButtonText: "Đóng",
    }).then(function(isConfirm) {
    });
};
function showNotificationContact(){
    swal({
        title: "Inform",
        text: "Send success",
        type: "success",
        confirmButtonClass: "btn-success",
        confirmButtonText: "Đóng",
    }).then(function(isConfirm) {
    });
};

function showNotificationRegister(result){
    swal({
        title: "Inform",
        text: "Register success",
        type: "success",
        showCancelButton: true,
        cancelButtonClass: "btn-success",
        confirmButtonText: "Login now",
        cancelButtonText: "Đóng",
    }).then(function(isConfirm) {
        if (isConfirm.dismiss != "cancel") {
            window.location.assign("/login?userName="+result.userName);
        }
    });
};

// get token in cookie
function getTokenInCookie(){
    let token = document.cookie.slice(document.cookie.lastIndexOf("=")+1);
    return token;
}