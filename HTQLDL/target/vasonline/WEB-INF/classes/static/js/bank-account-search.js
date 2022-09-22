let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/bank-account-search?agencyId="+$('#agencyId').val() + "\
		&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
            requestData.sort();
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.agencyBankAccounts + "</td><td>" + jsonItem.bankAccountNo +"</td><td>" + jsonItem.bankName +
                    "</td><td>" + jsonItem.bankBranch + "</td><td>" + jsonItem.bankAddress + "</td><td>" + jsonItem.createdDate + "</td><td>"+ jsonItem.updateDate + "</td>";
                htmlDOM += "<td align='center'>" +
                    "<button data-id='" + jsonItem.bankAccountId + "' data-title = '"+jsonItem.agencyBankAccounts+"' data-target='#modal-command-type-edit' class='action edit btn btn-primary-outline'>\
                <i class='fa fa-pencil' aria-hidden='true'></i></button>\
                <a href='/vasonline/delete-bank-account/" + jsonItem.id + "' class='action delete btn btn-primary-outline'>\
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
$(document).on("click", ".delete", function(e){
    e.preventDefault();
    var r = confirm("Bạn muốn xóa tài khoản ngân hàng đại lý?");
    if (r){
        window.location = $(this).attr('href');

    }
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
        url: '/vasonline/new-bank-account/',
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
    let idBankAccount = $(this).data('id');
    let nameAgency = $(this).data('title').toUpperCase();

    $.ajax({
        url: '/vasonline/edit-bank-account/' + idBankAccount,
        type: 'GET',
        success:function(data){
            $('#edit_table').html(data);
            $('#modal-command-type-edit').modal('toggle');
            $('#modal-title-edit').text('CHỈNH SỬA NGÂN HÀNG ĐẠI LÝ " ' + nameAgency + ' "');
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