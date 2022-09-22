let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;
$('#doSearch').on('click', function() {
    let totalPriceDiscount=0;
    let totalPriceBrand=0;
    let totalQuantityActive=0;
    let totalQuantityNotActive=0;
    let totalCostAmKam=0;
    pageSize = $('select[name=selector] option').filter(':selected').val();
    var linkGet="";
    if($('#agencyCode').val() != ""){
        linkGet = "/vasonline/search-report-detail?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#agencyCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val() +"\
		&page=" + pageNumber + "&size=" + pageSize
    }
    if ($('#amCode').val() != ""){
        linkGet = "/vasonline/search-report-detail?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#amCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val() +"\
		&page=" + pageNumber + "&size=" + pageSize
    }
    if ($('#agencyCode').val() == "" && $('#amCode').val() == ""){
        linkGet = "/vasonline/search-report-detail?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#agencyCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val() +"\
		&page=" + pageNumber + "&size=" + pageSize
    }
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + linkGet,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
            requestData.sort();
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                totalPriceBrand += jsonItem.price;
                totalQuantityActive += jsonItem.amount;
                totalPriceDiscount += jsonItem.discountAgency;
                totalCostAmKam += jsonItem.costAmKam;
                totalQuantityNotActive += jsonItem.amountUnactivated;
                htmlDOM += "<tr><td>" + jsonItem.customerName + "</td><td>" + jsonItem.customerId +"</td><td>" + jsonItem.serviceRequestId + "</td>" +
                    "<td>" + jsonItem.areaName + "</td><td>" + jsonItem.agencyCode + "</td><td>" + jsonItem.amCode + "</td><td>" + jsonItem.createdDate + "</td>" +
                    "<td>" + jsonItem.approvedDate + "</td><td>" + jsonItem.creator + "</td><td>" + jsonItem.brandGroupName + "</td><td>" + jsonItem.brandName + "</td>" +
                    "<td>" + jsonItem.brandId + "</td><td>" + numberWithCommas(jsonItem.price) + "</td><td>" + numberWithCommas(jsonItem.quantity) + "</td><td>" + jsonItem.rowNum + "</td>" +
                    "<td>" + jsonItem.asrCreatedDate + "</td><td>" + numberWithCommas(jsonItem.quantityActivations) + "</td><td>" + numberWithCommas(jsonItem.amount) + "</td><td>" + numberWithCommas(jsonItem.discountAgency) + "</td>" +
                    "<td>" + numberWithCommas(jsonItem.costAmKam) + "</td><td>" + numberWithCommas(jsonItem.quantityUnactivated) + "</td><td>" + numberWithCommas(jsonItem.amountUnactivated) + "</td></tr>";
            });
            htmlDOM +="<tr><th colspan='12'> Tổng </th><th>" + numberWithCommas(totalPriceBrand) + "</th><th colspan='4'></th>" +
                "<th>" + numberWithCommas(totalQuantityActive) + "</th><th>" + numberWithCommas(totalPriceDiscount) + "</th><th>" + numberWithCommas(totalCostAmKam) + "</th><th></th>" +
                "<th>" + numberWithCommas(totalQuantityNotActive) + "</th></tr>";
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
    var linkGet="";
    if($('#agencyCode').val() != ""){
        linkGet = "/vasonline/report/export-detail?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#agencyCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val()
    }
    if ($('#amCode').val() != ""){
        linkGet = "/vasonline/report/export-detail?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#amCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val()
    }
    if ($('#agencyCode').val() == "" && $('#amCode').val() == ""){
        linkGet = "/vasonline/report/export-detail?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#agencyCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val()
    }
    window.location.href = origin + linkGet;
})
function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}