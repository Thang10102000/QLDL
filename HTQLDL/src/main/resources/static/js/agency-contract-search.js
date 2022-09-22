let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function () {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/search-contract-agency?contractNo=" + $('#contractNoSearch').val() + "&agencyName=" + $('#agencyNameSearch').val()
            + "&status=" + $('#statusSearch').val() + "&serviceType=" + $('#serviceTypeSearch').val() +
        "&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function (data, responseText, status) {
            requestData = Object.keys(data);
            // requestData.sort();
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {

                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.contractNo + "</td><td>" + jsonItem.agencyName + "</td><td>" +  jsonItem.startDate + "</td><td>" + jsonItem.endDate  + "</td><td>" + jsonItem.createdDate  + "</td><td>" + jsonItem.updateDate  + "</td>";
                let checkJsonStatus = jsonItem.status;
                if (checkJsonStatus == 0) {
                    htmlDOM += "<td align='center'><span class='badge badge-secondary'>Mới tạo</span></td>";
                }
                if (checkJsonStatus == 1) {
                    htmlDOM += "<td align='center'><span class='badge badge-success'>Đã ký</span></td>";
                }
                if (checkJsonStatus == 2) {
                    htmlDOM += "<td align='center'><span class='badge badge-warning'>Đã thanh lý</span></td>";
                }
                if (checkJsonStatus == 3) {
                    htmlDOM += "<td align='center'><span class='badge badge-danger'>Hết hạn</span></td>";
                }
                if (checkJsonStatus == 4) {
                    htmlDOM += "<td align='center'><span class='badge badge-primary'>Đã gia hạn</span></td>";
                }
                let checkJsServiceType = jsonItem.serviceType;
                if (checkJsServiceType == 0) {
                    htmlDOM += "<td align='center'><span>Trả trước</span></td>";
                } else {
                    htmlDOM += "<td align='center'><span>Trả sau</span></td>";
                }
                htmlDOM += "<td align='center'><button  data-id='" + jsonItem.id + "' data-title = '"+jsonItem.agencyName+"' data-target='#modal-command-type-edit' class='action edit btn btn-primary-outline'>\
                <i class='fa fa-pencil' aria-hidden='true'></i></button>\
                <a href='/vasonline/agencyContract/delete/" + jsonItem.id + "' class='action delete btn btn-primary-outline'>\
                    <i class='fa fa-trash-o'></i></a></td></tr>"
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
//show modal create
$("#doNew").on('click',function () {
    $.ajax({
        url: '/vasonline/agencyContract/add/',
        type: 'GET',
        success:function(data){
            $('#create_table').html(data);
            $('#modal-command-type-create').modal('toggle');
        },
        error: function(jqXHR, exception){
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }
            console.log(jqXHR, exception);
        }
    });
});
//show modal EDIT
$(document).on('click',".edit",function () {
    let idAgencyContract = $(this).data('id');
    let agencyName = $(this).data('title');

    $.ajax({
        url: '/vasonline/agencyContract/edit/' + idAgencyContract,
        type: 'GET',
        success:function(data){
            $('#edit_table').html(data);
            $('#modal-command-type-edit').modal('toggle');
            $('#modal-title-edit').text('" ' + agencyName + ' "');
        },
        error: function(jqXHR, exception){
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }
            console.log(jqXHR, exception);
        }
    });
});