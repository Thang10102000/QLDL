var pageNumber2 = $('#currentPage2').text();
var pageSize2 = $('select[name=selector] option').filter(':selected').val();
var totalPage2 = 1;
var requestData2;

$('#doSearchDetail').on('click', function() {
    pageSize2 = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/search-kam-commission?policyName2="+$('#policyName2').val()+"&agencyName=" + $('#agencyName').val() + "\
		&page2=" + pageNumber2 + "&size2=" + pageSize2,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData2 = Object.keys(data);
            requestData2.sort();
            let htmlDOM = "<tbody>";
            requestData2.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.agencyName + "</td><td>" + jsonItem.policyName +"</td><td>" + jsonItem.startDate +
                    "</td><td>" + jsonItem.endDate + "</td>";
                htmlDOM += "<td align='center'><button data-id='" + jsonItem.id + "'  class='action deleteAgency btn btn-primary-outline'>\
                    <i class='fa fa-trash-o'></i></button></td></tr>"
            })
            htmlDOM += "</tbody>";
            document.getElementById("tableBody2").innerHTML = htmlDOM;
            totalPage = Object.values(data)[0];
            document.getElementById("currentPage2").innerHTML = pageNumber2;
            if (totalPage2 != undefined){
                document.getElementById("totalPage").innerHTML = "(Tổng số " + totalPage2 + " trang), Số bản ghi&nbsp";
            }else {
                document.getElementById("totalPage").innerHTML = "(Tổng số 0 trang), Số bản ghi&nbsp";
            }
        }, error: function() {
            // window.location.replace(window.location.origin+'/login?message=max_session');
        }
    });
})

function prevPage2() {
    pageSize2 = $('select[name=selector] option').filter(':selected').val();
    if (pageNumber2 == 1) {
        event.preventDefault();
    } else {
        if (pageNumber2 - 1 == 0) {
            pageNumber2 = 1;
            document.getElementById("currentPage2").innerHTML = pageNumber2;
        } else {
            pageNumber2--;
            document.getElementById("currentPage2").innerHTML = pageNumber2;
        }
        $('#doSearchDetail').click();
    }
}
function nextPage2() {
    pageSize2 = $('select[name=selector] option').filter(':selected').val();
    if (pageNumber2 < totalPage) {
        pageNumber2++;
        pageSize2 = $('select[name=selector] option').filter(':selected').val();
        document.getElementById("currentPage2").innerHTML = pageNumber2;
        $('#doSearchDetail').click();
    } else {
        event.preventDefault();
    }
}

function resetPage2() {
    pageNumber2 = 1;
    $('#doSearchDetail').click();
}

function lastPage2() {
    pageNumber2 = totalPage2;
    $('#doSearchDetail').click();
}

function firstPage2() {
    pageNumber2 = 1;
    $('#doSearchDetail').click();
}

$(document).on('click',".deleteAgency",function () {
    var id = $(this).data('id');
    //let rs = confirm("Bạn muốn xóa chính sách?");
    //if (rs) {
        $.ajax({
            url: '/vasonline/delete-kam-commission/' + id,
            method: 'GET',
            success: function (data) {
                if (data == 'success') {
                    toastr.success('Thành công');
                } else {
                    toastr.error('Thất bại');
                }

                $("#doSearchDetail").click();
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
$(function () {
    $("#doSearchDetail").click();
});