let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function () {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/agency-contract-search?contractNo=" + $('#contractNo').val() + "&agencyName=" + $('#agencyName').val()
            + "&status=" + $('#status').val() + "&startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val() +"\
    &page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function (data, responseText, status) {
            requestData = Object.keys(data);
           // requestData.sort();
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {

                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.contractNo + "</td><td>" + jsonItem.agencyName + "</td><td>" + jsonItem.areaName + "</td><td>"
               + numberWithCommas(jsonItem.guaranteeValue) + "</td><td>"+ jsonItem.signDate + "</td><td>" +  jsonItem.startDate + "</td><td>" + jsonItem.endDate  + "</td>";
                let checkJsonStatus = jsonItem.status;
                if (checkJsonStatus == 0) {
                    htmlDOM += "<td align='center'><span class='badge badge-secondary'>Mới tạo</span></td>";
                }
                if (checkJsonStatus == 1) {
                    htmlDOM += "<td align='center'><span class='badge badge-success'>Đã ký</span></td>";
                }
                if (checkJsonStatus == 2) {
                    htmlDOM += "<td align='center'><span class='badge badge-warning'>Đã thanh lý</span></td>";
                }
                if (checkJsonStatus == 3) {
                    htmlDOM += "<td align='center'><span class='badge badge-danger'>Hết hạn</span></td>";
                }
                if (checkJsonStatus == 4) {
                    htmlDOM += "<td align='center'><span class='badge badge-primary'>Đã gia hạn</span></td>";
                }
                let checkJsServiceType = jsonItem.serviceType;
                if (checkJsServiceType == 0) {
                    htmlDOM += "<td align='center'><span>Trả trước</span></td></tr>";
                } else {
                    htmlDOM += "<td align='center'><span>Trả sau</span></td></tr>";
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
        }, error: function () {
            //window.location.replace(window.location.origin+'/login?message=max_session');
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

$(document).on('click',"#export",function () {
    window.location.href = "/vasonline/agencyContract/export-agency-contract?contractNo=" + $('#contractNo').val() + "&agencyName=" + $('#agencyName').val()
        + "&status=" + $('#status').val() + "&startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val();
})
function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}