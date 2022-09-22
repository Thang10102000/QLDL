let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/agency-search?agencyName="+$('#agencyNameSearch').val()+"&areaId=" + $('#areaId').val() + "&status=" + $('#status').val() + "\
		&type=0" + "&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                if(jsonItem.updateDate === undefined)
                {
                    jsonItem.updateDate = "";
                }
                htmlDOM += "<tr><td>" + jsonItem.agencyName + "</td><td>" + jsonItem.areaId +"</td><td>" + jsonItem.surplus +"</td><td>"+ jsonItem.createdDate +"</td><td>"+ jsonItem.updateDate +"</td>";
                let checkJsonStatus = jsonItem.status;
                if (checkJsonStatus == 0) {
                    htmlDOM += "<td align='center'><span class='badge badge-success'>Hiệu lực</span></td>";
                } else {
                    htmlDOM += "<td align='center'><span class='badge badge-danger'>Không hiệu lực</span></td>";
                }
                htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.id + "' class='action edit btn btn-primary-outline' >\
                <i class='fa fa-pencil' aria-hidden='true'></i></button>\
                \<button data-id='" + jsonItem.id + "' data-toggle='tooltip' data-placement='top' title='Tài khoản ngân hàng' \
                 class='action bank-account btn btn-primary-outline' ><i class='fa fa-university'></i></button>\
                 \<button data-id='" + jsonItem.id + "' data-toggle='tooltip' data-placement='top' title='Hợp đồng' \
                 class='action agency-contracts btn btn-primary-outline' ><i class='fa fa-address-book'></i></button>\
                 \<button data-id='" + jsonItem.id + "' data-toggle='tooltip' data-placement='top' title='Bảo lãnh/ Đặt cọc' \
                 class='action deposite btn btn-primary-outline' ><i class='fa fa-credit-card'></i></button>\
                 \<button data-id='" + jsonItem.id + "' data-toggle='tooltip' data-placement='top' title='Thông tin liên hệ' \
                 class='action authorized btn btn-primary-outline' ><i class='fa fa-tty'></i></button>\
                <a href='/vasonline/delete-agency/" + jsonItem.id + "' class='action delete btn btn-primary-outline'>\
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
$(document).on("click", ".delete", function(e) {
    e.preventDefault();
    var r = confirm("Bạn muốn xóa đại lý?");
    if (r==true){
        window.location = $(this).attr('href');
    }
})

//show modal create
$("#doNew").on('click',function () {
    $.ajax({
        url: '/vasonline/new-agency/',
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

//show modal
$(document).on('click',".edit",function () {
    let idAgency = $(this).data('id');
    $.ajax({
        url: '/vasonline/edit-agency/' + idAgency,
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
$(document).on('click','.bank-account',function () {
    let agencyId = $(this).data('id');
    let areaId = $("#areaId").val();
    if(areaId != ""){
        localStorage.removeItem("areaId");
        localStorage.setItem("areaId",areaId)
    }else {
        $.ajax({
            url: "/vasonline/get-bank-area-id?agencyId=" + agencyId,
            method: "GET",
            success: function (data) {
                console.log(data);
                areaId = data;
                localStorage.removeItem("areaId");
                localStorage.setItem("areaId",areaId);
            },
            error:function (error) {
                console.log(error.responseText);
            }
        })
    }
    localStorage.removeItem("agencyId");
    localStorage.setItem("agencyId", agencyId);
    window.location.href = "bank-account";
})
$(document).on('click','.agency-contracts',function () {
    let agencyId = $(this).data('id');
    localStorage.removeItem("agencyIdForContracts");
    localStorage.setItem("agencyIdForContracts", agencyId);
    let areaId = $("#areaId").val();
    if(areaId != ""){
        localStorage.removeItem("areaIdForContracts");
        localStorage.setItem("areaIdForContracts",areaId)
    }else {
        $.ajax({
            url: "/vasonline/get-bank-area-id?agencyId=" + agencyId,
            method: "GET",
            success: function (data) {
                console.log(data);
                areaId = data;
                localStorage.removeItem("areaIdForContracts");
                localStorage.setItem("areaIdForContracts",areaId);
            },
            error:function (error) {
                console.log(error.responseText);
            }
        })
    }
    window.location.href = "agencyContract/index";
})
$(document).on('click','.deposite',function () {
    let agencyId = $(this).data('id');
    localStorage.removeItem("agencyIdForDeposite");
    localStorage.setItem("agencyIdForDeposite", agencyId);

    let areaId = $("#areaId").val();
    if(areaId != ""){
        localStorage.removeItem("areaIdForDeposite");
        localStorage.setItem("areaIdForDeposite",areaId)
    }else {
        $.ajax({
            url: "/vasonline/get-bank-area-id?agencyId=" + agencyId,
            method: "GET",
            success: function (data) {
                console.log(data);
                areaId = data;
                localStorage.removeItem("areaIdForDeposite");
                localStorage.setItem("areaIdForDeposite",areaId);
            },
            error:function (error) {
                console.log(error.responseText);
            }
        })
    }

    window.location.href = "deposite/index";
})
$(document).on('click','.authorized',function () {
    let agencyId = $(this).data('id');
    localStorage.removeItem("agencyIdForAuthorized");
    localStorage.setItem("agencyIdForAuthorized", agencyId);

    let areaId = $("#areaId").val();
    if(areaId != ""){
        localStorage.removeItem("areaIdForAuthorized");
        localStorage.setItem("areaIdForAuthorized",areaId)
    }else {
        $.ajax({
            url: "/vasonline/get-bank-area-id?agencyId=" + agencyId,
            method: "GET",
            success: function (data) {
                console.log(data);
                areaId = data;
                localStorage.removeItem("areaIdForAuthorized");
                localStorage.setItem("areaIdForAuthorized",areaId);
            },
            error:function (error) {
                console.log(error.responseText);
            }
        })
    }

    window.location.href = "authorized";
})
