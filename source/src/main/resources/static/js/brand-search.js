let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/brand-search?brandId="+$('#brandIdSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&status=" + $('#statusSearch').val() +
		"&brandType=" + $('#brandTypeSearch').val()+"&brandGroupBId=" + $('#brandGroupNameSearch').val() + "&approvedBy=" + $('#level').val() +"&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
           // requestData.sort();
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.brandId + "</td><td>" + jsonItem.brandName +"</td><td>" + jsonItem.brandGroupB +
                    "</td><td>" + numberWithCommas(jsonItem.price) + "</td><td>" + jsonItem.discountRate +"</td><td>"+ numberWithCommas(jsonItem.priceDiscount) +"</td><td>"+jsonItem.dcPolicyId+"</td><td>"+jsonItem.createdDate+"</td><td>"+jsonItem.updateDate+"</td>" ;
                let checkJsBrandType = jsonItem.brandType;
                if (checkJsBrandType == 0) {
                    htmlDOM += "<td align='center'><span>Trả trước</span></td>";
                } else {
                    htmlDOM += "<td align='center'><span>Trả sau</span></td>";
                }
                let checkJsonStatus = jsonItem.status;
                if (checkJsonStatus == 0) {
                    htmlDOM += "<td align='center'><span class='badge badge-success'>Hiệu lực</span></td>";
                } else {
                    htmlDOM += "<td align='center'><span class='badge badge-danger'>Không hiệu lực</span></td>";
                }
                htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.id + "' class='action edit btn btn-primary-outline' >\
                <i class='fa fa-pencil' aria-hidden='true'></i></button>\
                <a href='/vasonline/delete-brand/" + jsonItem.id + "' class='action delete btn btn-primary-outline'>\
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
$("#doNew").on('click',function () {
    $.ajax({
        url: '/vasonline/new-brand/',
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
$(document).on('click',".edit",function () {
    let idBrand= $(this).data('id');
    $.ajax({
        url: '/vasonline/edit-brand/' + idBrand,
        type: 'GET',
        success: function (data) {
            $('#edit_table').html(data);
            $('#modal-command-type-edit').modal('toggle');
        },
        error: function (jqXHR, exception) {
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }
            console.log(jqXHR, exception);
        }
    });
})
function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}
