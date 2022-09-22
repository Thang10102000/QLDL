let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function () {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/agency-order-search?agencyName=" + $('#agencyNameSearch').val() + "&agencyArea=" + $('#selectCTKVSearch').val() + "&orderId=" + $('#orderIdSearch').val() + "&status=" + $('#statusSearch').val() + "\
		&startDate=" + $('#startDateSearch').val() + "&endDate=" + $('#endDateSearch').val() + "&paymentMethod="+ $('#paymentMethodSearch').val() + "\
		&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function (data, responseText, status) {
            requestData = Object.keys(data);
            // requestData.sort();
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                //jsonItem.orderStatusAOName = undefined;
                // let currentAmount = 0;
                // if(jsonItem.remainingAmount < jsonItem.orderPay) {currentAmount=jsonItem.orderPay - jsonItem.remainingAmount};
                htmlDOM += "<tr><td>" + jsonItem.orderId + "</td><td>" + jsonItem.agencyArea + "</td><td>" + jsonItem.agencyAO + "</td><td>" + numberWithCommas(jsonItem.orderValue) + "</td><td>" + numberWithCommas(jsonItem.orderPay)
                    + "</td><td>"+ numberWithCommas(jsonItem.consumptionAmount) + "</td><td>" + jsonItem.startDate + "</td><td>" + jsonItem.endDate + "</td><td>" + jsonItem.createdDate + "</td><td>" + jsonItem.updateDate + "</td>";
                let checkPayment = jsonItem.paymentMethod;
                console.log(jsonItem.orderStatusAOName);
                if (checkPayment == 0){
                    htmlDOM += "<td align='center'>Trả trước</td>";
                }
                if(checkPayment == 1) {
                    htmlDOM += "<td align='center'>Trả sau</td>";
                }
                if(checkPayment == 2) {
                    htmlDOM += "<td align='center'>Tài khoản</td>";
                }
                let checkJsonStatus = jsonItem.orderStatusAO;
                if (checkJsonStatus == 0) {
                    htmlDOM += "<td align='center'><span class='badge badge-secondary'>" + jsonItem.orderStatusAOName + "</span></td>";
                }
                if (checkJsonStatus == 1) {
                    htmlDOM += "<td align='center'><span class='badge badge-warning'>" + jsonItem.orderStatusAOName + "</span></td>";
                }
                if (checkJsonStatus == 2) {
                    htmlDOM += "<td align='center'><span class='badge badge-primary'>" + jsonItem.orderStatusAOName + "</span></td>";
                }
                if (checkJsonStatus == 3) {
                    htmlDOM += "<td align='center'><span class='badge badge-success'>" + jsonItem.orderStatusAOName + "</span></td>";
                }
                if (checkJsonStatus == 4) {
                    htmlDOM += "<td align='center'><span class='badge badge-danger'>" + jsonItem.orderStatusAOName + "</span></td>";
                }
                if (checkJsonStatus == 5) {
                    htmlDOM += "<td align='center'><span class='badge badge-warning'>" + jsonItem.orderStatusAOName + "</span></td>";
                }
                if (checkJsonStatus == 6) {
                    htmlDOM += "<td align='center'><span class='badge badge-primary'>" + jsonItem.orderStatusAOName + "</span></td>";
                }
                if (checkJsonStatus == 7) {
                    htmlDOM += "<td align='center'><button class='list_act btn btn-primary-outline' data-id='"+ jsonItem.orderId + "' title='Chi tiết'>" +
                        "<span class='badge badge-warning'>" + jsonItem.orderStatusAOName + "</span></button></td>";
                }
                if (checkJsonStatus == 8) {
                    htmlDOM += "<td align='center'><button class='list_act btn btn-primary-outline' data-id='"+ jsonItem.orderId + "' title='Chi tiết'>" +
                        "<span class='badge badge-success'>" + jsonItem.orderStatusAOName + "</span></button>  </td>";
                }
                if (checkJsonStatus == 9) {
                    htmlDOM += "<td align='center'><span class='badge badge-danger'>" + jsonItem.orderStatusAOName + "</span></td>";
                }
                // các button liên quan
                let level = jsonItem.levelId;
                //Trạng thái mới tạo và đơn hàng trả sau
                if (checkJsonStatus == 0 && checkPayment != 0) {
                    if(level == 1) {
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/agency-order-status/" + jsonItem.orderId + "' class='btn btn-primary-outline browse' data-toggle='tooltip' \
                        data-placement='top' title='Chuyển duyệt'><i class='fa fa-arrow-circle-o-right' ></i></a>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-edit' class='action edit btn btn-primary-outline'\
                        data-toggle='tooltip' data-placement='top' title='Sửa'><i class='fa fa-pencil' aria-hidden='true'></i></button>\
                        <a href='/vasonline/delete-agency-order/" + jsonItem.orderId + "' class='action delete btn btn-primary-outline'\
                         data-toggle='tooltip' data-placement='top' title='Xoá'><i class='fa fa-trash-o'></i></a>\
                        <a href='/vasonline/cancel-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline closed' data-toggle='tooltip' \
                        data-placement='top' title='Hủy'><i class='fa fa-close'></i></a></td></tr>"
                    }
                    else{
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/agency-order-status/" + jsonItem.orderId + "' class='btn btn-primary-outline browse' data-toggle='tooltip' \
                        data-placement='top' title='Chuyển duyệt'><i class='fa fa-arrow-circle-o-right' ></i></a>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-edit' class='action edit btn btn-primary-outline'\
                        data-toggle='tooltip' data-placement='top' title='Sửa'><i class='fa fa-pencil' aria-hidden='true'></i></button>\
                        <a href='/vasonline/delete-agency-order/" + jsonItem.orderId + "' class='action delete btn btn-primary-outline'\
                         data-toggle='tooltip' data-placement='top' title='Xoá'><i class='fa fa-trash-o'></i></a></td></tr>"
                    }
                }
                //Trạng thái mới tạo và đơn hàng trả trước
                if (checkJsonStatus == 0 && checkPayment == 0) {
                    if(level == 1) {
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/wait-pay-order/" + jsonItem.orderId + "' class='btn btn-primary-outline wait-pay' data-toggle='tooltip' \
                        data-placement='top' title='Chờ thanh toán'><i class='fa fa-cc-mastercard'></i></a>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-edit' class='action edit btn btn-primary-outline'\
                        data-toggle='tooltip' data-placement='top' title='Sửa'><i class='fa fa-pencil' aria-hidden='true'></i></button>\
                        <a href='/vasonline/delete-agency-order/" + jsonItem.orderId + "' class='action delete btn btn-primary-outline'\
                         data-toggle='tooltip' data-placement='top' title='Xoá'><i class='fa fa-trash-o'></i></a>\
                        <a href='/vasonline/cancel-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline closed' data-toggle='tooltip' \
                        data-placement='top' title='Hủy'><i class='fa fa-close'></i></a></td></tr>"
                    }
                    else{
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/wait-pay-order/" + jsonItem.orderId + "' class='btn btn-primary-outline wait-pay' data-toggle='tooltip' \
                        data-placement='top' title='Chờ thanh toán'><i class='fa fa-cc-mastercard'></i></a>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-edit' class='action edit btn btn-primary-outline'\
                        data-toggle='tooltip' data-placement='top' title='Sửa'><i class='fa fa-pencil' aria-hidden='true'></i></button>\
                        <a href='/vasonline/delete-agency-order/" + jsonItem.orderId + "' class='action delete btn btn-primary-outline'\
                         data-toggle='tooltip' data-placement='top' title='Xoá'><i class='fa fa-trash-o'></i></a></td></tr>"
                    }
                }
                //Trạng thái chờ duyệt
                if (checkJsonStatus == 1) {
                    if (level ==2){
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/active-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline actived' data-toggle='tooltip' \
                        data-placement='top' title='Kích hoạt'><i class='fa fa-key'></i></a></td></tr>"
                    }
                    else if(level == 1){
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/active-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline actived' data-toggle='tooltip' \
                        data-placement='top' title='Kích hoạt'><i class='fa fa-key'></i></a>\
                        <a href='/vasonline/delete-agency-order/" + jsonItem.orderId + "' class='action delete btn btn-primary-outline'\
                         data-toggle='tooltip' data-placement='top' title='Xoá'><i class='fa fa-trash-o'></i></a></td></tr>"
                    }
                    else {
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button></td></tr>"
                    }
                }
                //Trạng thái chờ thanh toán
                if (checkJsonStatus == 5) {
                    if (level == 1){
                        htmlDOM += "<td align='center'>\
                         <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-pay-confirm' data-value='"+jsonItem.orderPay+"' class='btn pay-confirm btn-primary-outline'\
                         data-toggle='tooltip' data-placement='top' title='Thanh toán'><i class='fa fa-credit-card'></i></button>\
                         <a href='/vasonline/cancel-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline closed' data-toggle='tooltip' \
                        data-placement='top' title='Hủy'><i class='fa fa-close'></i></a></td></tr>"
                    }else {
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button></td></tr>"
                    }

                }
                //Trạng thái đã thanh toán và còn nợ
                // if (checkJsonStatus == 6 && jsonItem.remainingAmount - jsonItem.orderPay < 0) {
                //     if (level == 1){
                //         htmlDOM += "<td align='center'>\
                //         <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                //         data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                //          <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-pay-confirm-update' data-value='"+jsonItem.orderPay+"' class='btn pay-confirm-update btn-primary-outline'\
                //           data-toggle='tooltip' data-placement='top' title='Thanh toán'><i class='fa fa-credit-card'></i></button> \
                //          <a href='/vasonline/active-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline actived' data-toggle='tooltip' \
                //         data-placement='top' title='Kích hoạt'><i class='fa fa-key'></i></a></td></tr>"
                //     }else {
                //         htmlDOM += "<td align='center'>\
                //         <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                //         data-placement='top' title='Xem'><i class='fa fa-eye'></i></button></td></tr>"
                //     }
                //
                // }
                //Trạng thái đã thanh toán và số tiền cần thanh toán = 0
                // if (checkJsonStatus == 6 && jsonItem.remainingAmount - jsonItem.orderPay >= 0){
                if (checkJsonStatus == 6){
                    // && jsonItem.currentValue == 0) {
                    if (level == 1){
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/active-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline actived' data-toggle='tooltip' \
                        data-placement='top' title='Kích hoạt'><i class='fa fa-key'></i></a>\
                        <a href='/vasonline/cancel-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline closed' data-toggle='tooltip' \
                        data-placement='top' title='Hủy'><i class='fa fa-close'></i></a></td></tr>"
                    }
                    else if(level == 2){
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/active-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline actived' data-toggle='tooltip' \
                        data-placement='top' title='Kích hoạt'><i class='fa fa-key'></i></a></td></tr>"
                    }
                    else {
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button></td></tr>"
                    }

                }
                //Trạng thái đã thanh toán và số tiền cần thanh toán > 0
                // if (checkJsonStatus == 3 && jsonItem.currentValue > 0) {
                //     if (level == 1){
                //         htmlDOM += "<td align='center'>\
                //         <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                //         data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                //         <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-call' class='btn btn-primary-outline call' data-toggle='tooltip' \
                //         data-placement='top' title='Xuất hoá đơn'><i class='fa fa-sign-out'></i></button>\
                //         <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-pay-confirm-update' data-value='"+jsonItem.orderPay+"' class='btn pay-confirm-update btn-primary-outline'\
                //          data-toggle='tooltip' data-placement='top' title='Thanh toán'><i class='fa fa-credit-card'></i></button></td></tr> "
                //     }else {
                //         htmlDOM += "<td align='center'>\
                //         <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                //         data-placement='top' title='Xem'><i class='fa fa-eye'></i></button></td></tr> "
                //     }
                //
                // }
                //Trạng thái đã thanh toán
                if ((checkJsonStatus == 3 || checkJsonStatus == 4)){
                    // && jsonItem.currentValue == 0) {
                    if(level == 1){
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-call' class='btn btn-primary-outline call' data-toggle='tooltip' \
                        data-placement='top' title='Xuất hoá đơn'><i class='fa fa-sign-out'></i></button>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/cancel-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline closed' data-toggle='tooltip' \
                        data-placement='top' title='Hủy'><i class='fa fa-close'></i></a></td></tr>"
                    }
                    else {
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-call' class='btn btn-primary-outline call' data-toggle='tooltip' \
                        data-placement='top' title='Xuất hoá đơn'><i class='fa fa-sign-out'></i></button>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\</td></tr>"
                    }
                }
                //Trạng thái đã thanh toán, hết hạn và đẩy hoá đơn bán hàng sang bhtt
                if ( checkJsonStatus == 7 ){
                    if(level == 1) {
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-call' class='btn btn-primary-outline call' data-toggle='tooltip' \
                        data-placement='top' title='Xuất hoá đơn'><i class='fa fa-sign-out'></i></button>\
                        <a href='/vasonline/call-api-success/" + jsonItem.orderId + "' class='action btn btn-primary-outline call-success' data-toggle='tooltip' \
                        data-placement='top' title='Hoàn thành'><i class='fa fa-check'></i></a>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\
                        <a href='/vasonline/cancel-agency-order/" + jsonItem.orderId + "' class='action btn btn-primary-outline closed' data-toggle='tooltip' \
                        data-placement='top' title='Hủy'><i class='fa fa-close'></i></a></td></tr>"
                    }
                    else{
                        htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-call' class='btn btn-primary-outline call' data-toggle='tooltip' \
                        data-placement='top' title='Xuất hoá đơn'><i class='fa fa-sign-out'></i></button>\
                        <a href='/vasonline/call-api-success/" + jsonItem.orderId + "' class='action btn btn-primary-outline call-success' data-toggle='tooltip' \
                        data-placement='top' title='Hoàn thành'><i class='fa fa-check'></i></a>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\</td></tr>"
                    }
                }

            //    trạng thái đã hoàn thành xuất hoá đơn
                if ( checkJsonStatus == 8 ){
                    htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\</td></tr>"
                }
                // trạng thái hủy đơn hàng
                if ( checkJsonStatus == 9 ){
                    htmlDOM += "<td align='center'>\
                        <button data-id='" + jsonItem.orderId + "' data-target='#modal-command-type-view' class='btn btn-primary-outline view' data-toggle='tooltip' \
                        data-placement='top' title='Xem'><i class='fa fa-eye'></i></button>\</td></tr>"
                }
            })
            htmlDOM += "</tbody>";
            document.getElementById("tableBody").innerHTML = htmlDOM;
            totalPage = Object.values(data)[0];
            document.getElementById("currentPage").innerHTML = pageNumber;
            if (totalPage != undefined){
                document.getElementById("totalPage").innerHTML = "(Tổng số " + totalPage + " trang), Số bản ghi&nbsp";
            }else {
                document.getElementById("totalPage").innerHTML = "(Tổng số 0 trang), Số bản ghi&nbsp";
            }
        }, error: function () {
            //window.location.replace(window.location.origin+'/login?message=max_session');
        }
    });
})


function prevPage() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    if (pageNumber == 1) {
        event.preventDefault();
    } else {
        if (pageNumber - 1 == 0) {
            pageNumber = 1;
            document.getElementById("currentPage").innerHTML = pageNumber;
        } else {
            pageNumber--;
            document.getElementById("currentPage").innerHTML = pageNumber;
        }
        $('#doSearch').click();
    }
}

function nextPage() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    if (pageNumber < totalPage) {
        pageNumber++;
        pageSize = $('select[name=selector] option').filter(':selected').val();
        document.getElementById("currentPage").innerHTML = pageNumber;
        $('#doSearch').click();
    } else {
        event.preventDefault();
    }
}

function resetPage() {
    pageNumber = 1;
    $('#doSearch').click();
}

function lastPage() {
    pageNumber = totalPage;
    $('#doSearch').click();
}

function firstPage() {
    pageNumber = 1;
    $('#doSearch').click();
}

//show modal thêm mới
$(document).on('click',"#doNewSearch",function () {
    $.ajax({
        url: '/vasonline/new-agency-order/',
        type: 'GET',
        success:function(data){
            $('#create_table').html(data);
            $('#modal-command-type-create').modal('toggle');
        },
        error: function (jqXHR,exception) {
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }

        }
    });
});

//show modal chỉnh sửa
$(document).on('click',".edit",function () {
    let orderId = $(this).data('id');
    $.ajax({
        url: '/vasonline/edit-agency-order/' + orderId,
        type: 'GET',
        success:function(data){
            $('#edit_table').html(data);
            $('#modal-command-type-edit').modal('toggle');
        },
        error: function (jqXHR,exception) {
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }

        }
    });
});

//show modal chi tiết
$(document).on('click',".view",function () {
    let orderId = $(this).data('id');
    $.ajax({
        url: '/vasonline/view-agency-order/' + orderId,
        type: 'GET',
        success:function(data){
            $('#detail_table').html(data);
            $('#modal-command-type-detail').modal('toggle');
        },
        error: function (jqXHR,exception) {
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }

        }
    });
});

//show modal xác nhận thanh toán
$(document).on('click',".pay-confirm",function () {
    let orderId = $(this).data('id');
    let orderPay = $(this).data('value');
    $.ajax({
        url: '/vasonline/pay-confirm/' + orderId + '/' + orderPay,
        type: 'GET',
        success:function(data){
            $('#pay_confirm_table').html(data);
            $('#modal-command-type-pay-confirm').modal('toggle');
        },
        error: function (jqXHR,exception) {
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }

        }
    });
});

//show modal xác nhận thanh toán số tiên còn nợ
$(document).on('click',".pay-confirm-update",function () {
    let orderId = $(this).data('id');
    let orderPay = $(this).data('value');
    $.ajax({
        url: '/vasonline/pay-confirm-update/' + orderId + '/' + orderPay,
        type: 'GET',
        success:function(data){
            $('#pay_confirm_update_table').html(data);
            $('#modal-command-type-pay-confirm-update').modal('toggle');
        },
        error: function (jqXHR,exception) {
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }

        }
    });
});

//show modal call api
$(document).on('click',".call",function () {
    let orderId = $(this).data('id');
    $.ajax({
        url: '/vasonline/agency-order-call-transaction/' + orderId,
        type: 'GET',
        success:function(data){
            $('#call_table').html(data);
            $('#modal-command-type-call').modal('toggle');
        },
        error: function (jqXHR,exception) {
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }

        }
    });
});

//show list activate
$(document).on('click',".list_act",function () {
    let requestId = $(this).data('id');
    $.ajax({
        url: '/vasonline/list-order-active/' + requestId,
        type: 'GET',
        success:function(data){
            $('#list_act_table').html(data);
            $('#modal-command-type-list-act').modal('toggle');
        },
        error: function(jqXHR, exception){
            if(jqXHR.status == 403){
                messageNotAuthorized();
            }
            else{
                messageSystemError()
            }
            console.log(jqXHR, exception);
        }
    });
});

function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}

$(function() {
    // $("#doSearch").click();
    var delayInMilliseconds = 500;

    setTimeout(function() {
        //your code to be executed after delayInMilliseconds second
        $("#doSearch").click();
    }, delayInMilliseconds);
    // $("#doSearch").click();
})