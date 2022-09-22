let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;
let col1 =0;let col2 =0;let col3 =0;let col4 =0;let col5 =0;let col6 =0;let col7 =0;let col8 =0;
let col9 =0;let col10 =0;let col11 =0;let col12 =0;let col13 =0;let col14 =0;let col15 =0;let col16 =0;
let col17=0;
$('#doSearch').on('click', function() {
    pageSize = $('select[name=selector] option').filter(':selected').val();
    $.ajax({
        async: false,
        type: 'GET',
        url: origin + "/vasonline/search-report-agency-area?brandGroupName="+$('#brandGroupNameSearch').val()+"&brandName=" + $('#brandNameSearch').val() +"&areaName=" + $('#areaId').val() + "&createdDate=" + $('#createdDate').val() + "&endDate=" + $('#endDate').val() + "&approvedDate=" + $('#approvedDate').val() + "&approvedEndDate=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val() +"\
		&page=" + pageNumber + "&size=" + pageSize,
        dataType: 'json',
        success: function(data, responseText, status) {
            requestData = Object.keys(data);
            let htmlDOM = "<tbody>";
            requestData.forEach((item) => {
                let jsonItem = JSON.parse(item);
                col1 += jsonItem.sum_amount;col2 +=  jsonItem.sum_revenue_brand_activated;col3 += jsonItem.sum_quantity_ck;
                col4 += jsonItem.sum_quantity_new_ck;col5 += jsonItem.sum_quantity_brand_activated;col6 += jsonItem.sum_quantity_brand_unactivated ;
                col7 += jsonItem.sum_amount_ck; col8 += jsonItem.sum_revenue_brand_acti_ck;col9 += jsonItem.agencyDiscount;
                col10 += jsonItem.sum_quantity_hh;col11 += jsonItem.sum_quantity_new_hh;col12 += jsonItem.sum_quantity_brand_acti;
                col13 += jsonItem.sum_quantity_brand_unacti;col14 += jsonItem.sum_amount_hh;col15 += jsonItem.sum_revenue_brand_acti_hh;
                col16 += jsonItem.commission_rate;col17 += jsonItem.total_cost_of_service_commission;
                    htmlDOM += "<tr><td>" + jsonItem.areaName + "</td><td align='center'>" + numberWithCommas(jsonItem.sum_amount) +"</td><td align='center'>" + numberWithCommas(jsonItem.sum_revenue_brand_activated) + "</td>" +
                        "<td align='center'>" + numberWithCommas(jsonItem.sum_quantity_ck) + "</td><td align='center'>" + numberWithCommas(jsonItem.sum_quantity_new_ck) + "</td><td align='center'>" + numberWithCommas(jsonItem.sum_quantity_brand_activated) + "</td><td align='center'>" + numberWithCommas(jsonItem.sum_quantity_brand_unactivated) + "</td>" +
                        "<td align='center'>" + numberWithCommas(jsonItem.sum_amount_ck)+ "</td><td align='center'>" + numberWithCommas(jsonItem.sum_revenue_brand_acti_ck) + "</td><td align='center'>" + numberWithCommas(jsonItem.agencyDiscount) + "</td><td align='center'>" + numberWithCommas(jsonItem.sum_quantity_hh) + "</td>" +
                        "<td align='center'>" + numberWithCommas(jsonItem.sum_quantity_new_hh) + "</td><td align='center'>" + numberWithCommas(jsonItem.sum_quantity_brand_acti) + "</td><td align='center'>" + numberWithCommas(jsonItem.sum_quantity_brand_unacti) + "</td><td align='center'>" + numberWithCommas(jsonItem.sum_amount_hh) + "</td>" +
                        "<td align='center'>" + numberWithCommas(jsonItem.sum_revenue_brand_acti_hh) + "</td><td align='center'>" + jsonItem.commission_rate + "</td><td align='center'>" + numberWithCommas(jsonItem.total_cost_of_service_commission) + "</td></tr>";
            });
            htmlDOM +="<tr><th> Tổng </th><th>" + numberWithCommas(col1) + "</th>" +
                "<th>" + numberWithCommas(col2) + "</th><th>" + numberWithCommas(col3) + "</th>" +
                "<th>" + numberWithCommas(col4) + "</th><th>" + numberWithCommas(col5) + "</th>" +
                "<th>" + numberWithCommas(col6) + "</th><th>" + numberWithCommas(col7)+ "</th>" +
                "<th>" + numberWithCommas(col8) + "</th><th>" + numberWithCommas(col9) + "</th>" +
                "<th>" + numberWithCommas(col10) + "</th><th>" + numberWithCommas(col11) + "</th>" +
                "<th>" + numberWithCommas(col12) + "</th><th>" + numberWithCommas(col13) + "</th>" +
                "<th>" + numberWithCommas(col14) + "</th><th>" + numberWithCommas(col15) + "</th>" +
                "<th>" + numberWithCommas(col16) + "</th><th>" + numberWithCommas(col17) + "</th></tr>";
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
    window.location.href = origin + "/vasonline/report/export-agency-area-report?brandGroupName="+$('#brandGroupNameSearch').val()+
        "&brandName=" + $('#brandNameSearch').val() +"&areaName=" + $('#areaId').val() + "&createdDate=" +
        $('#createdDate').val() + "&endDate=" + $('#endDate').val() + "&approvedDate=" + $('#approvedDate').val() +
        "&approvedEndDate=" + $('#dateEnd').val() +"&reportMonth=" + $('#monthReport').val()
})
function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
}