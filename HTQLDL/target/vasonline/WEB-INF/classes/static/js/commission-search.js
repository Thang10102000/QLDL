let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    let linkPost = "";
    if($('#isDefaultSearch').prop('checked')){
        linkPost = "/vasonline/commission-search?policyName="+$('#policyNameSearch').val()+"&isDefault=1&startDate=" + $('#startDateSearch').val() + "&endDate=" + $('#endDateSearch').val() + "\
		&page=" + pageNumber + "&size=" + pageSize;
    }else {
        linkPost = "/vasonline/commission-search?policyName="+$('#policyNameSearch').val()+"&isDefault=&startDate=" + $('#startDateSearch').val() + "&endDate=" + $('#endDateSearch').val() + "\
		&page=" + pageNumber + "&size=" + pageSize;
    }

    $.ajax({
        async: false,
        type: 'GET',
        url: origin + linkPost,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
            requestData.sort();
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.policyName + "</td><td>" + jsonItem.startDate +"</td><td>" + jsonItem.endDate +
                    "</td><td>" + jsonItem.rate + "</td><td>" + jsonItem.createdDate + "</td><td>"+ jsonItem.updateDate + "</td>";
                htmlDOM += "<td align='center'>" +
                    "<button  data-id='" + jsonItem.id + "' data-title = '"+jsonItem.policyName+"' data-target='#modal-command-type-edit' class='action edit btn btn-primary-outline'>\
                <i class='fa fa-pencil' aria-hidden='true'></i></button>\
                <button  data-id='" + jsonItem.id + "' data-title = '"+jsonItem.policyName+"' data-target='#modal-command-type-detail' class='action detail btn btn-primary-outline'>\
                <i class=\"fa fa-eye\" aria-hidden=\"true\"></i></button>\
                <a href='/vasonline/delete-commission/" + jsonItem.id + "' class='action delete btn btn-primary-outline'>\
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
        url: '/vasonline/new-commission/',
        type: 'GET',
        success:function(data){
            $('#create_table').html(data);
            $('#modal-command-type-create').modal('toggle');
        },
        error: function(jqXHR, exception){
            console.log(jqXHR, exception);
        }
    });
});

//show modal EDIT
$(document).on('click',".edit",function () {
    let idCommission = $(this).data('id');
    let policyName = $(this).data('title');

    $.ajax({
        url: '/vasonline/edit-commission/' + idCommission,
        type: 'GET',
        success:function(data){
            $('#edit_table').html(data);
            $('#modal-command-type-edit').modal('toggle');
            $('#modal-title-edit').text('" ' + policyName + ' "');
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

//show modal detail
$(document).on('click',".detail", function () {
    let idCommission = $(this).data('id');
    let policyName = $(this).data('title');

    $.ajax({
        url: '/vasonline/detail-commission/' + idCommission,
        type: 'GET',
        success:function(data){
            $('#detail_table').html(data);
            $('#modal-command-type-detail').modal('toggle');
            $('#modal-title-detail').text('" ' + policyName + ' "');
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