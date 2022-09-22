var pageNumber1 = $('#currentPage1').text();
var pageSize1 = $('select[name=selector1] option').filter(':selected').val();
var totalPage1 = 1;
var requestData1;

$('#doSearchBrand').on('click', function() {
    pageSize1 = $('select[name=selector1] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/search-brand-commission?policyName1="+$('#policyName1').val()+"&brandName=" + $('#brandName').val() + "\
		&page=" + pageNumber1 + "&size=" + pageSize1,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData1 = Object.keys(data);
            requestData1.sort();
            let htmlDOM = "<tbody>";
            requestData1.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.policyName + "</td><td>" + jsonItem.brandName +"</td><td>" + jsonItem.startDate +
                    "</td><td>" + jsonItem.endDate + "</td><td>" + jsonItem.commissionRate +"</td>";
                htmlDOM += "<td align='center'><button data-id='" + jsonItem.id + "' class='action deleteBrand btn btn-primary-outline'>\
                    <i class='fa fa-trash-o'></i></button></td></tr>"
            })
            htmlDOM += "</tbody>";
            document.getElementById("tableBody1").innerHTML = htmlDOM;
            totalPage1 = Object.values(data)[0];
            document.getElementById("currentPage1").innerHTML = pageNumber1;
            if (totalPage1 != undefined){
                document.getElementById("totalPage").innerHTML = "(Tổng số " + totalPage1 + " trang), Số bản ghi&nbsp";
            }else {
                document.getElementById("totalPage").innerHTML = "(Tổng số 0 trang), Số bản ghi&nbsp";
            }
        }, error: function() {
            // window.location.replace(window.location.origin+'/login?message=max_session');
        }
    });
})

function prevPage1() {
    pageSize1 = $('select[name=selector1] option').filter(':selected').val();
    if (pageNumber1 == 1) {
        event.preventDefault();
    } else {
        if (pageNumber1 - 1 == 0) {
            pageNumber1 = 1;
            document.getElementById("currentPage1").innerHTML = pageNumber1;
        } else {
            pageNumber1--;
            document.getElementById("currentPage1").innerHTML = pageNumber1;
        }
        $('#doSearchBrand').click();
    }
}
function nextPage1() {
    pageSize1 = $('select[name=selector1] option').filter(':selected').val();
    if (pageNumber1 < totalPage1) {
        pageNumber1++;
        pageSize1 = $('select[name=selector1] option').filter(':selected').val();
        document.getElementById("currentPage1").innerHTML = pageNumber1;
        $('#doSearch').click();
    } else {
        event.preventDefault();
    }
}

function resetPage1() {
    pageNumber1 = 1;
    $('#doSearchBrand').click();
}

function lastPage1() {
    pageNumber1 = totalPage1;
    $('#doSearchBrand').click();
}

function firstPage1() {
    pageNumber1 = 1;
    $('#doSearchBrand').click();
}
$(function () {
    $("#doSearchBrand").click();
});

$(document).on('click',".deleteBrand",function () {
    var id = $(this).data('id');
    //let rs = confirm("Bạn muốn xóa chính sách?");
    //if (rs) {
    $.ajax({
        url: '/vasonline/delete-brand-commission/' + id,
        method: 'GET',
        success: function (data) {
            if (data == 'success') {
                toastr.success('Thành công');
            } else {
                toastr.error('Thất bại');
            }
            $("#doSearchBrand").click();
        },
        error: function (jqXHR) {
            if (jqXHR.status == 403){
                toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
            }else {
                toastr.error('Lỗi hệ thống!');
            }
        }

    });
    // }
    // this.rs = '';
});