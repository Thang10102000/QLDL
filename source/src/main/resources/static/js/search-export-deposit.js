let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
    let linkPost = "/vasonline/search-deposit-agency?agencyName="+$('#agencyNameSearch').val()+"&status=" + $('#statusSearch').val()+"&depositeAmount=" + $('#depositeAmountSearch').val()
        + "&startDate=" + $('#startDateSearch').val() + "&endDate=" + $('#endDateSearch').val() + "&page=" + pageNumber + "&size=" + pageSize;
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + linkPost,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.agencyName + "</td><td>" + numberWithCommas(jsonItem.depositsAmount) +"</td><td>" + jsonItem.startDate +"</td><td>" + jsonItem.endDate +
                    "</td>";
                let checkJsonStatus = jsonItem.status;
                if (checkJsonStatus == 1) {
                    htmlDOM += "<td align='center'><span class='badge badge-success'>Hiệu lực</span></td></tr>";
                } else {
                    htmlDOM += "<td align='center'><span class='badge badge-danger'>Không hiệu lực</span></td></tr>";
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

$(document).on('click',"#export",function () {
    window.location.href = "/vasonline/deposite/export-deposit?agencyName="+$('#agencyNameSearch').val()+"&status=" + $('#statusSearch').val()+"&depositeAmount=" + $('#depositeAmountSearch').val()
        + "&startDate=" + $('#startDateSearch').val() + "&endDate=" + $('#endDateSearch').val()
})
function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}

