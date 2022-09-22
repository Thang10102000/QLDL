let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;
$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/search-report-revenue?brandGroupId=" + $('#brandGroupNameSearch').val() + "&brandId=" + $('#brandNameSearch').val() + "&costAmKam=" + $('#agencyName').val()
            + "&areaId=" + $('#areaId').val() + "&amountUnactivated=" + $('#monthReport').val() + "&createdDate=" + $('#toDate').val() + "&endDate=" + $('#endDate').val()
            + "&approvedDate=" + $('#approvedDate').val() + "&fromDate=" + $('#fromDate').val() + "\
		&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function (data, responseText, status) {
            requestData = Object.keys(data);
            let totalMoney = 0;
            let totalService = 0;
            let totalDiscount = 0;
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                htmlDOM += "<tr><td>" + jsonItem.serviceName + "</td><td>" + jsonItem.agencyCode + "</td><td>" + jsonItem.brandGroupName + "</td>" +
                    "<td>" + jsonItem.brandName + "</td><td>" + numberWithCommas(jsonItem.quantity) + "</td><td>" + numberWithCommas(jsonItem.brandNew) + "</td><td>" + numberWithCommas(jsonItem.brandEt) + "</td>" +
                    "<td>" + numberWithCommas(jsonItem.brandOff) + "</td><td>" + numberWithCommas(jsonItem.totalMoney) + "</td><td>" + numberWithCommas(jsonItem.countBrand) + "</td><td>" + jsonItem.discountRate + "</td></tr>";
                totalMoney += jsonItem.totalMoney;
                totalService += jsonItem.countBrand;
                totalDiscount += jsonItem.discountRate;
            })
            htmlDOM += "<tr><th>"  +"Tổng "+ "</th><th>" + "</th><th>" + "</th>" +
                "<th>" + "</th><th>" + "</th><th>" + "</th><th>" + "</th>" +
                "<th>" + "</th><th>" + numberWithCommas(totalMoney) + "</th><th>" + numberWithCommas(totalService) + "</th><th>" + numberWithCommas(totalDiscount) + "</th></tr>";
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
            // window.location.replace(window.location.origin+'/login?message=max_session');
        }
    });
});

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
        linkGet = "/vasonline/report/export-revenue?brandGroupId=" + $('#brandGroupNameSearch').val() + "&brandId=" + $('#brandNameSearch').val() + "&costAmKam=" + $('#agencyName').val()
            + "&areaId=" + $('#areaId').val() + "&amountUnactivated=" + $('#monthReport').val() + "&createdDate=" + $('#toDate').val() + "&approvedDate=" + $('#fromDate').val()
        window.location.href = origin + linkGet;
    });
function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}