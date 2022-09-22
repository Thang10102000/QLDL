let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function (e) {
    e.preventDefault();
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/pay-confirm-search?orderId=" + $('#orderId').val() + "&paymentMethod=" + $('#paymentMethod').val() + "&amount=" + $('#amount').val() + "\
		&receiverName=" + $('#receiverName').val() + "&receiverDate=" + $('#receiverDate').val() + "\
		&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function (data, responseText, status) {
            requestData = Object.keys(data);
            requestData.sort();
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {

                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.orderId + "</td><td>" + jsonItem.amount + "</td><td>" + jsonItem.paymentMethod
                    + "</td><td>" + jsonItem.receiverName + "</td><td>" + jsonItem.receiverTime + "</td><td>" + jsonItem.description + "</td></tr>";
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
