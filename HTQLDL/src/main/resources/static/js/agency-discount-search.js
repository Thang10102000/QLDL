var pageNumber1 = $('#currentPage1').text();
var pageSize1 = $('select[name=selector] option').filter(':selected').val();
var totalPage1 = 1;
var requestData1;

$('#doSearchDetail').on('click', function() {
    pageSize1 = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/search-agency-discount?policyName1="+$('#policyName1').val()+"&agencyName=" + $('#agencyName').val() + "\
		&page=" + pageNumber1 + "&size=" + pageSize1,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData1 = Object.keys(data);
            requestData1.sort();
            let htmlDOM = "<tbody>";
            requestData1.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.areaName + "</td><td>" + jsonItem.agencyName +"</td><td>" + jsonItem.policyName +
                    "</td>";
                htmlDOM += "<td align='center'><button data-id='" + jsonItem.id + "' class='action deleteAG btn btn-primary-outline'>\
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

function prevPage() {
    pageSize1 = $('select[name=selector] option').filter(':selected').val();
    if (pageNumber1 == 1) {
        event.preventDefault();
    } else {
        if (pageNumber1 - 1 == 0) {
            pageNumber1 = 1;
            document.getElementById("currentPage").innerHTML = pageNumber1;
        } else {
            pageNumber1--;
            document.getElementById("currentPage").innerHTML = pageNumber1;
        }
        $('#doSearchDetail').click();
    }
}
function nextPage1() {
    pageSize1 = $('select[name=selector] option').filter(':selected').val();
    if (pageNumber1 < totalPage1) {
        pageNumber1++;
        pageSize1 = $('select[name=selector] option').filter(':selected').val();
        document.getElementById("currentPage").innerHTML = pageNumber1;
        $('#doSearchDetail').click();
    } else {
        event.preventDefault();
    }
}

function resetPage1() {
    pageNumber1 = 1;
    $('#doSearchDetail').click();
}

function lastPage1() {
    pageNumber1 = totalPage1;
    $('#doSearchDetail').click();
}

function firstPage1() {
    pageNumber1 = 1;
    $('#doSearchDetail').click();
}
$(function () {
    $("#doSearchDetail").click();
});
$(document).on('click',".deleteAG",function () {
    var id = $(this).data('id');
    //let rs = confirm("Bạn muốn xóa chính sách?");
    //if (rs) {
    $.ajax({
        url: '/vasonline/delete-agency-discount/' + id,
        method: 'GET',
        success: function (data) {
            if (data == 'success') {
                toastr.success('Thành công');
            } else {
                toastr.error('Thất bại');
            }

            $("#doSearchDetail").click();
        },
        error: function (jqXHR,exception) {
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