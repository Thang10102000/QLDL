let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/system-attr/search?saId="+$('#saId').val() + "&type="+$('#type').val()+"&name=" + $('#name').val() + "&value=" + $('#value').val() + "&description=" + $('#description').val() + "&page="+ pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function(data) {
            requestData = Object.keys(data);
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.saId + "</td><td>" + jsonItem.type + "</td><td>" + jsonItem.name +"</td><td>" + jsonItem.value +"</td><td>" + jsonItem.description +"</td><td>" + jsonItem.modified +"</td><td>" + jsonItem.modifier +"</td>";
                htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.saId + "' class='action edit btn btn-primary-outline' >\
                <i class='fa fa-pencil' aria-hidden='true'></i></button> \
                <a href='/vasonline/system-attr/delete/" + jsonItem.saId + "' class='action delete btn btn-primary-outline' id='"+jsonItem.saId+"'>\
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

$(document).on("click", ".delete", function(event) {
    event.preventDefault();
    var id = $(this).attr('id');
    var r = confirm("Bạn có muốn xóa tham số hệ thống mã: " + id + "?");
    if (r) {
        window.location = $(this).attr('href');
    }
});

//show modal create
$("#doAdd").on('click',function () {
    $.ajax({
        url: '/vasonline/system-attr/create',
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

//show modal edit
$(document).on('click',".edit",function () {
    let id = $(this).data('id');
    $.ajax({
        url: '/vasonline/system-attr/edit/' + id,
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
})