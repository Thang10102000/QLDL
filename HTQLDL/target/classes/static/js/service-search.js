let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;


$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/service-search?serviceId="+$('#serviceIdSearch').val()+"&serviceName=" + $('#serviceNameSearch').val() + "&shortCode=" + $('#shortCodeSearch').val() + "\
		&status=" + $('#statusSearch').val() + "\
		&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.serviceId + "</td><td>" + jsonItem.serviceName +"</td><td>" + jsonItem.shortCode + "</td><td>" + jsonItem.description + "</td><td>" + jsonItem.createdDate + "</td><td>" + jsonItem.updateDate + "</td>";
                let checkJsonStatus = jsonItem.status;
                if (checkJsonStatus == 0) {
                    htmlDOM += "<td align='center'><span class='badge badge-success'>Hiệu lực</span></td>";
                } else {
                    htmlDOM += "<td align='center'><span class='badge badge-danger'>Không hiệu lực</span></td>";
                }
                htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.id + "' class='action edit btn btn-primary-outline' >\
                <i class='fa fa-pencil' aria-hidden='true'></i></button>\
                <button data-id='" + jsonItem.id + "' class='action transaction btn btn-primary-outline'  >\
                <i class='fa fa-arrow-circle-right'></i></button>\
                <a href='/vasonline/delete-service/" + jsonItem.id + "' class='action delete btn btn-primary-outline'>\
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
        }, error: function() {
            // window.location.replace(window.location.origin+'/login?message=max_session');
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
        url: '/vasonline/new-service/',
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

//show modal
$(document).on('click',".edit",function () {
    let idService = $(this).data('id');
    $.ajax({
        url: '/vasonline/edit-service/' + idService,
        type: 'GET',
        success:function(data){
            $('#edit_table').html(data);
            $('#modal-command-type-edit').modal('toggle');
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
})

$(document).on('click','.transaction',function () {
    let serviceId = $(this).data('id');
    localStorage.removeItem("serviceId");
    localStorage.setItem("serviceId", serviceId);
    window.location.href = "brand-group";
})