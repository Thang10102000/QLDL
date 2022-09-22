$(document).ready(function () {
    $(".select2").select2();
    $("#allAgency").prop("checked", true);
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
    $(':checkbox').change(function () {
        if ($("#allAgency").prop('checked') == true) {
            $(".notAllAgency").attr("hidden", true);
        } else {
            $(".notAllAgency").removeAttr('hidden');
        }
        if($('#isDefault').prop('checked')){
            $("#checkDefault").val("1");
        }else {
            $("#checkDefault").val("");
        }
    });
    $("#startDate").datepicker({
        format: "dd/mm/yyyy",
        startDate: 'today',
        todayHighlight: true
    }).datepicker("setDate", new Date());
    $("#endDate").datepicker({
        format: "dd/mm/yyyy",
        startDate: 'today',
        todayHighlight: true
    });
    $("#agencyDiscounts").on("change", function (e) {
        let value = Array.from(e.target.selectedOptions, option => option.value);
        $('input:hidden[name=agency]').val(value);
    })
    $("#selectCTKV").on("change", function (e) {
        let value = Array.from(e.target.selectedOptions, option => option.value);
        // $('input:hidden[name=brand]').val(value);
        var linkPost = "/vasonline/agency-list-no-policy?idAgencyArea=" + value.toString();

        $.ajax({
            url: linkPost,
            method: 'GET',
            success: function (data) {

                var str = '';

                for (let i = 0; i < data.length; i++) {
                    if(data[i].status == 0){
                        str += ' <option value= "' + data[i].id + '" >' + data[i].agencyName + '</option>'
                    }
                }
                $('#agencyDiscounts').html(str);

            },
            error: function (request) {
                alert("The request failed: " + request.responseText);
            }
        });
    })
    $("#policyName, #minOrder, #limitOrder, #agencyDiscounts").keydown(function () {
        $("#policyNameMessage, #discountRateMessage, #minOrderMessage, #limitOrderMessage, #agencyDiscountsMessage").text("");
    })
});
$(" #startDate, #endDate").on("change",function () {
    $(" #startDateMessage, #endDateMessage").text("");
})

//add minOrder, limitOrder, discountRate

var attArray = [{
    index: 0
}];
var arrIndex = 0;
function addDiscountRate() {
    // $(".addAttr").prop("hidden", true);
    // $(".deleteAttr").prop("hidden", true);
    // $(".title-plus").prop("hidden", true);
    arrIndex += 1;
    attArray.push({
        index: arrIndex
    });
    let html = "";
    for (let i = arrIndex; i < attArray.length; i++) {
        html += "<tr><td><label for='minOrder" + attArray[i].index + "'>Hạn mức từ(VNĐ)<b class='required'>(*)</b></label>\n" +
            "<input type='text' name='minOrder" + attArray[i].index + "' class='form-control formatMoney' data-type='currency' \n" +
            " placeholder='Hạn mức dưới' id='minOrder" + attArray[i].index + "' autocomplete='off'>\n" +
            "<span class='required' id='minOrderMessage" + attArray[i].index + "'></span></td>";
        html += "<td><label for='limitOrder" + attArray[i].index + "'>Hạn mức đến(VNĐ)<b class='required'>(*)</b></label>\n" +
            "<input type='text' name='limitOrder" + attArray[i].index + "' class='form-control formatMoney' \n" +
            " data-type='currency' placeholder='Hạn mức trên' id='limitOrder" + attArray[i].index + "' autocomplete='off'>\n" +
            "<span class='required' id='limitOrderMessage" + attArray[i].index + "'></span></td>";
        html += "<td><label for='discountRate" + attArray[i].index + "'>Tỷ lệ chiết khấu(%) <b class='required'>(*)</b></label>\n" +
            "<input type='number' name='discountRate" + attArray[i].index + "' class='form-control' placeholder='Tỷ lệ chiết khấu'\n" +
            "id='discountRate" + attArray[i].index + "' min='0' max='100'><span class='required' id='discountRateMessage" + attArray[i].index + "'></span></td>";
        // html += "<td><label class='title-plus'> Thêm </label><br><a class='addAttr' onclick='addDiscountRate()'><i class='fa fa-plus'></i></a></td>";
        html += "<td><label class='title-plus'> Xoá </label><br><a class='deleteAttr vasDeleteRow'><i class='fa fa-minus'></i></a></td></tr>";
    }
    $(html).insertBefore(".ctl-discount");
    $('#minOrder1').val($('#limitOrder').val());
    if(arrIndex > 1){
        for (let i = arrIndex; i < attArray.length; i++){
            $('#minOrder' + i).val($('#limitOrder' + attArray[i-1].index).val());
        }
    }
}
function removeDiscountRate(index){}
$("#tbDiscount").on('click', '.vasDeleteRow', function () {
    $(this).closest('tr').remove();
});
//validate
$(document).on('click', "#doNew" ,function (e) {
    let format = /[`!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
    let minOrder = parseInt($("#minOrder").val().replace(/,/g, ''));
    let limitOrder = parseInt($("#limitOrder").val().replace(/,/g, ''));
    $("#lengthArr").val(attArray.length);
    if (format.test($("#policyName").val())) {
        $("#policyNameMessage").text("Tên chính sách chứa ký tự đặc biệt");
        return false;
    }
    if ($("#policyName").val() === '') {
        $("#policyNameMessage").text("Bạn chưa nhập tên chính sách");
        return false;
    }
    if ($("#commisionRate").val() === '') {
        $("#commisionRateMessage").text("Bạn chưa nhập tỷ lệ hoa hồng");
        return false;
    }
    if ($("#startDate").val() === '') {
        $("#startDateMessage").text("Bạn chưa nhập ngày bắt đầu");
        return false;
    }
    if ($("#endDate").val() === '') {
        $("#endDateMessage").text("Bạn chưa nhập ngày kết thúc");
        return false;
    }

    //	check end date > start date
    var ngaybatdau = $('#startDate').val();
    var ngayketthuc = $('#endDate').val();
    ngaybatdau = parseDate(ngaybatdau).getTime();
    ngayketthuc = parseDate(ngayketthuc).getTime();

    if (ngaybatdau > ngayketthuc) {
        $('#endDateMessage').text('Vui lòng nhập ngày kết thúc lớn hơn ngày bắt đầu!');
        return false;
    }

    if ($("#minOrder").val() === '') {
        $("#minOrderMessage").text("Bạn chưa nhập hạn mức dưới");
        return false;
    }
    if ($("#limitOrder").val() === '') {
        $("#limitOrderMessage").text("Bạn chưa nhập hạn mức trên");
        return false;
    }
    if(minOrder > limitOrder){
        $("#limitOrderMessage").text("Hạn mức trên phải lớn hơn hạn mức dưới");
        return false;
    }
    if ($("#discountRate").val() * 1 < 0 || $("#discountRate").val() * 1 > 100) {
        $("#discountRateMessage").text("Tỷ lệ chiết khấu không đúng định dạng");
        return false;
    }
    if ($("#allAgency").prop("checked", false)) {
        if ($("#agencyDiscounts").val() === '') {
            $("#agencyDiscountsMessage").text("Bạn chưa nhập đại lý");
            return false;
        }
    }
    for (let i = 1; i < attArray.length; i++){
        if ($("#minOrder" + attArray[i].index).val() === '') {
            $("#minOrderMessage"+ attArray[i].index).text("Bạn chưa nhập hạn mức dưới");
            return false;
        }
        if ($("#limitOrder"+ attArray[i].index).val() === '') {
            $("#limitOrderMessage"+ attArray[i].index).text("Bạn chưa nhập hạn mức trên");
            return false;
        }
        if(parseInt($("#minOrder" + attArray[i].index).val().replace(/,/g, '')) > parseInt($("#limitOrder"+ attArray[i].index).val().replace(/,/g, ''))){
            $("#limitOrderMessage"+ attArray[i].index).text("Hạn mức trên phải lớn hơn hạn mức dưới");
            return false;
        }
        if ($("#discountRate"+ attArray[i].index).val() * 1 < 0 || $("#discountRate"+ attArray[i].index).val() * 1 > 100) {
            $("#discountRateMessage"+ attArray[i].index).text("Tỷ lệ chiết khấu không đúng định dạng");
            return false;
        }
    }
});
for (let i = 1; i < attArray.length; i++){
    $("#minOrder" + attArray[i].index).on('keydown',function () {
        $("#minOrderMessage"+ attArray[i].index).text("");
    })
    $("#limitOrder"+ attArray[i].index).on('keydown',function () {
        $("#limitOrderMessage"+ attArray[i].index).text("");
    })
    $("#discountRate"+ attArray[i].index).on('keydown',function () {
        $("#discountRateMessage"+ attArray[i].index).text("");
    })
}
var invalidChars = ["-", "+", "e", ","];
$("#commisionRate").on("keydown", function (e) {
    if (invalidChars.includes(e.key)) {
        e.preventDefault();
    }
});

//parser from string to date is validate
function parseDate(str) {
    var mdy = str.split('/');
    return new Date(mdy[2], mdy[1], mdy[0]);
}