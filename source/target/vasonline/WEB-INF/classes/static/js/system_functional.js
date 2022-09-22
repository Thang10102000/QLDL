//const origin = window.location.origin;
let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/admin/system-functional/search?sfId=" + $('#sfId').val() + "&sfName=" + $('#sfName').val() + "&urlController=" + $('#urlController').val() + "&sfFather=" + $('#sfFather').val() + "&sfStatus=" + $('#sfStatus').val() + "&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function(data) {
            requestData = Object.keys(data);
            console.log(requestData);
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                let status = "";
                if(jsonItem.status == '1') {
                    status = "Chức năng";
                }
                else{
                    status = "Nhóm chức năng";
                }
                htmlDOM += "<tr><td><b>" + jsonItem.sfId + "</b></td><td>" + jsonItem.fontAwesomeIconClass + "</td><td>" + jsonItem.sfName + "</td><td>" + jsonItem.urlController + "</td><td>" + status + "</td><td>" + jsonItem.sfFather + "</td><td align='center'>\
				<a class='btn btn-primary-outline view' data-id='"+ jsonItem.sfId + "' data-toggle='modal' data-target='#sfModal' title='Xem'> <i class='fa fa-eye'></i></a>\
				<button data-target='#modal-command-type-edit' data-id='"+ jsonItem.sfId + "' class='action edit btn btn-primary-outline' >\
				<i class='fa fa-pencil' aria-hidden='true'></i></button>\
				<a class='btn btn-primary-outline delete' id='group-"+ jsonItem.sfId + "' href='/vasonline/admin/system-functional/delete/" + jsonItem.sfId + "' title='Xóa'><i class='fa fa-trash-o' ></i ></a></td></tr > ";
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
        }
    });
})

let selectedSFId;

function findData(item, index) {
    let eachSF = JSON.parse(item);
    if (eachSF.sfId == selectedSFId) {
        $(".modal-sfId").text(eachSF.sfId);
        $(".modal-fontAwesomeIconClass").text(eachSF.fontAwesomeIconClass);
        $(".modal-sfName").text(eachSF.sfName);
        $(".modal-urlController").text(eachSF.urlController);
        if(eachSF.status == '1') {
            $(".modal-status").text("Chức năng");
        }
        else{
            $(".modal-status").text("Nhóm chức năng");
        }
        // $(".modal-status").text(eachSF.status);
        $(".modal-sfFather").text(eachSF.sfFather);
    }
}

$(document).on("click", ".view", function() {
    selectedSFId = $(this).data('id');
    $(".modal-content #systemFuntionalId").text(selectedSFId);
    requestData.forEach(findData)
})

//show modal create
$("#doNew").on('click',function () {
    $.ajax({
        url: '/vasonline/admin/system-functional/create/',
        type: 'GET',
        success:function(data){
            $('#create_table').html(data);
            $('#modal-command-type-create').modal('toggle');
            console.log("create SF success");
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
    let sfId = $(this).data('id');
    $.ajax({
        url: '/vasonline/admin/system-functional/edit/' + sfId,
        type: 'GET',
        success:function(data){
            $('#edit_table').html(data);
            $('#modal-command-type-edit').modal('toggle');
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

function messageNotAuthorized(){
    toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
}

function messageSystemError(){
    toastr.error('Lỗi hệ thống!');
}

$(document).on("click", ".delete", function(e) {
    e.preventDefault();
    var r = confirm("Bạn muốn xóa chức năng hệ thống?");
    if (r==true){
        window.location = $(this).attr('href');
    }
})
