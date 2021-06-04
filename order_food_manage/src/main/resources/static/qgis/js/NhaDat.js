$(function () {
    $('.button_close').click(function () {
        $("#div_bando").removeClass("col-lg-8");
        $("#div_bando").removeClass("p-0");
        $("#div_bando").addClass("col-lg-12");
        $("#div_Details").css('display', 'none');
    })
})