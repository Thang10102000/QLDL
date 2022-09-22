let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;
$('#doSearch').on('click', function() {
    let index=0;
    let totalPriceDiscount=0;
    let totalPriceBrand=0;
    let totalQuantityActive=0;
    let totalQuantityNotActive=0;
    let totalRevenue=0;
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/search-service-request-report?serviceId="+$('#service').val()+"&brandGId=" + $('#brandGroup').val() + "&brandId=" + $('#brand').val() +
            "&startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val() + "&serviceRequestId="+$("#serviceRequest").val()+  "&agencyCode="+$("#agency").val()
            + "&customerId="+$("#customer").val() + "&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
            let htmlDOM = "<tbody>";
            let totalPriceDiscountArr = [];
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                index += 1;
                totalPriceDiscountArr.push(jsonItem);
                console.log(totalPriceDiscountArr);
                // totalPriceDiscount += jsonItem.priceDiscount;
                // totalPriceBrand += jsonItem.priceBrand;
                 totalQuantityActive += jsonItem.quantityActive;
                // totalQuantityNotActive += jsonItem.quantityNotActive;
                totalRevenue += jsonItem.revenue;
                htmlDOM += "<tr><td>"+ index +"</td><td>" + jsonItem.serviceRequestId + "</td><td>" + jsonItem.customerName +  "</td><td>" + jsonItem.service + "</td><td>" + jsonItem.brandGroup +"</td><td>" + jsonItem.brand + "</td>" +
                    "<td>" + jsonItem.policy + "</td><td align='center'>" + numberWithCommas(jsonItem.price) + "</td><td align='center'>" + numberWithCommas(jsonItem.priceDiscount) + "</td><td align='center'>" + numberWithCommas(jsonItem.quantity) + "</td>" +
                    "<td align='center'>" + numberWithCommas(jsonItem.priceBrand) + "</td><td align='center'>" + jsonItem.quantityActive + "</td><td align='center'>" + jsonItem.quantityNotActive + "</td>" +
                    "<td>" + jsonItem.activeDate + "</td><td align='center'>" + numberWithCommas(jsonItem.revenue) + "</td></tr>";
            });
            for (let i = 0; i < totalPriceDiscountArr.length ; i++){
                if (i + 1 < totalPriceDiscountArr.length){
                    if (totalPriceDiscountArr[i].serviceRequestId != totalPriceDiscountArr[i+1].serviceRequestId){
                        totalPriceDiscount += totalPriceDiscountArr[i].priceDiscount;
                        totalPriceBrand += totalPriceDiscountArr[i].priceBrand;
                        totalQuantityNotActive += totalPriceDiscountArr[i].quantityNotActive;
                    }
                }else {
                    totalPriceDiscount += totalPriceDiscountArr[i].priceDiscount;
                    totalPriceBrand += totalPriceDiscountArr[i].priceBrand;
                    totalQuantityNotActive += totalPriceDiscountArr[i].quantityNotActive;
                }
            }
            htmlDOM +="<tr><th colspan='8'> Tổng </th><th>" + numberWithCommas(totalPriceDiscount) + "</th><th></th>" +
                "<th>" + numberWithCommas(totalPriceBrand) + "</th><th>" + numberWithCommas(totalQuantityActive) + "</th><th>" + numberWithCommas(totalQuantityNotActive) + "</th><th></th>" +
                "<th>" + numberWithCommas(totalRevenue) + "</th></tr>";
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

$('#doExport').on('click', function() {
    window.location.href = origin + "/vasonline/report/export-service-request-report?serviceId="+$('#service').val()+"&brandGId=" + $('#brandGroup').val() + "&brandId=" + $('#brand').val() +
        "&startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val()+ "&serviceRequestId="+$("#serviceRequest").val()+  "&agencyCode="+$("#agency").val()
        + "&customerId="+$("#customer").val();
});
function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}