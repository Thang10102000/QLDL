let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;
$('#doSearch').on('click', function() {
    let total4=0;
    let total1=0;
    let total2=0;
    let total3=0;
    pageSize = $('select[name=selector] option').filter(':selected').val();
    var linkGet="";
    if($('#agencyCode').val() != ""){
        linkGet = "/vasonline/search-report-commission?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#agencyCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val() +"\
		&page=" + pageNumber + "&size=" + pageSize
    }
    if ($('#amCode').val() != ""){
        linkGet = "/vasonline/search-report-commission?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#amCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val() +"\
		&page=" + pageNumber + "&size=" + pageSize
    }
    if ($('#agencyCode').val() == "" && $('#amCode').val() == ""){
        linkGet = "/vasonline/search-report-commission?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#agencyCode').val() +
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
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                total1 += jsonItem.quantityServiceRequestActivate;
                total2 += jsonItem.revenueBrand;
                total3 += jsonItem.commissionRate;
                total4 += jsonItem.totalCost;
                htmlDOM += "<tr><td>" + jsonItem.serviceName + "</td><td>" + jsonItem.agencyCode +"</td><td>" + jsonItem.brandGrName + "</td>" +
                    "<td>" + jsonItem.brandName + "</td><td align='center'>" + jsonItem.quantityNewOrder + "</td><td align='center'>" + jsonItem.quantityNewBrand + "</td><td align='center'>" + jsonItem.quantityActivationsBrand + "</td>" +
                    "<td align='center'>" + jsonItem.quantityUnactivatedBrand + "</td><td align='center'>" + jsonItem.quantityServiceRequestActivate +"</td><td align='center'>" + numberWithCommas(jsonItem.revenueBrand) + "</td><td align='center'>" + jsonItem.commissionRate + "</td><td align='center'>" + numberWithCommas(jsonItem.totalCost) + "</td></tr>";

            });
            htmlDOM +="<tr><th colspan='8'> Tổng </th><th>" + numberWithCommas(total1) + "</th>" +
                "<th>" + numberWithCommas(total2) + "</th><th>" + numberWithCommas(total3) + "</th><th>" + numberWithCommas(total4) + "</th></tr>";
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
        linkGet = "/vasonline/report/export-commission?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#agencyCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val()
    }
    if ($('#amCode').val() != ""){
        linkGet = "/vasonline/report/export-commission?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#amCode').val() +
            "&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() +
            "&approvedDate=" + $('#approvedDate').val() + "&dateEnd=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val()
    }
    if ($('#agencyCode').val() == "" && $('#amCode').val() == ""){
        linkGet = "/vasonline/report/export-commission?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() + "&agencyCode=" + $('#agencyCode').val() +
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