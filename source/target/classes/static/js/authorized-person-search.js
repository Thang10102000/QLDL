let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/authorized-search?agencyName1="+$('#agencyName1').val() + "&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
            requestData.sort().reverse();
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.agencyIdPerson + "</td><td>" + jsonItem.fullName +"</td><td>" + jsonItem.birthday +"</td><td>"
                   + jsonItem.position +"</td><td>" + jsonItem.phoneNumber +"</td><td>" + jsonItem.email +"</td>";
                let checkJsType = jsonItem.type;
                if (checkJsType == 0) {
                    htmlDOM += "<td align='center'><span>Người đại diện</span></td>";
                }
                if (checkJsType == 1){
                    htmlDOM += "<td align='center'><span>Người ủy quyền</span></td>";
                }
                if (checkJsType == 2){
                    htmlDOM += "<td align='center'><span>Đầu mối liên hệ</span></td>";
                }
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
    var r = confirm("Bạn muốn xóa thông tin đại lý?");
    if (r==true){
        window.location = $(this).attr('href');
    }
})