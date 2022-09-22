let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;
let levelIdUser =  $("#levelIdUser").text();

$('#doSearch').on('click', function() {
	pageSize = $('select[name=selector] option').filter(':selected').val();
	$.ajax({
		async: false,
		type: 'GET',
		url: origin + "/vasonline/service-request/search?packages=" + $('#package').val() + "&customer=" + $('#customer').val() + "&agencyArea=" + $('#agency_area').val() + "&status=" + $('#status').val() +"\
		&sr_id=" + $('#sr_id').val() + "&policy=" + $('#policy').val() + "&createdDateFrom=" + $('#createdDateFrom').val() + "&createdDateTo=" + $('#createdDateTo').val() + "\
		&approvalDateFrom="+$('#approvalDateFrom').val()+"&approvalDateTo="+$('#approvalDateTo').val()+"&page=" + pageNumber + "&size=" + pageSize,
		dataType: 'json',
		success: function(data) {
			requestData = Object.keys(data);
			// requestData.sort();
			let htmlDOM = "<tbody>";
			requestData.forEach((item) => {
				let jsonItem = JSON.parse(item);
				let policy;
				if(jsonItem.policy == 'HH'){
					policy = 'Hoa Hồng';
				}
				else if (jsonItem.policy == 'CK'){
					policy = 'Chiết khấu';
				}
				else {
					policy = 'Miễn phí'
				}
				let checkJsonStatus = jsonItem.status;
				htmlDOM += "<tr><td>" + jsonItem.srId + "</td><td>"+jsonItem.agencyArea+ "</td><td><a data-id='" + jsonItem.customerName + "' class='action external btn btn-primary-outline'>" + jsonItem.customerName + "</a></td><td><a data-id='" + jsonItem.brandName + "' class='action externalBr btn btn-primary-outline'>" + jsonItem.brandName
				+ "</td><td>" + policy + "</td><td>"+numberWithCommas(jsonItem.price + jsonItem.discountCost)+"</td><td>"+numberWithCommas(jsonItem.discountCost)+"</td><td>"
				 + jsonItem.quantity + "</td><td>"+numberWithCommas(jsonItem.amount)+"</td><td>"+jsonItem.created+"</td>";
				if (jsonItem.approved != null) {
					htmlDOM += "<td>" + jsonItem.approved + "</td>";
				} else {
					htmlDOM += "<td></td>";
				}
				let brandLevelId = jsonItem.brandLevelId;
				switch (checkJsonStatus) {
					case 0:
						htmlDOM += "<td align='center'><span class='badge badge-secondary'>Mới tạo</span></td>";
						htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.srId + "' class='action edit btn btn-primary-outline' >\
							<i class='fa fa-pencil-square-o' aria-hidden='true'></i></button>\
							<a class='action delete btn btn-primary-outline' id='"+jsonItem.srId+"' href='/vasonline/service-request/delete/"+jsonItem.srId+"'>\
							<i class='fa fa-trash-o' title='Xóa'></i></a>";
						if(levelIdUser == 2 ||levelIdUser == 3 || levelIdUser == 4 || levelIdUser == 1) {
							htmlDOM += "<a class='action accept btn btn-primary-outline' id='"+jsonItem.srId+"' href='/vasonline/service-request/accept/"+jsonItem.srId+"'>\
							<i class='fa fa-check-square-o' title='Chuyển duyệt'></i></a>"
							if(levelIdUser == 1){
								htmlDOM += "<a class='action btn btn-primary-outline cancel' id='"+jsonItem.srId+"' href='/vasonline/service-request/cancel/"+jsonItem.srId+"'>\
								<i class='fa fa-close' title='Huỷ đơn'></i></a>"
							}
							else{
								htmlDOM += "</td></tr>";
							}
						}
						else {
							htmlDOM += "</td></tr>";
						}
						break;
					case 1:
						htmlDOM += "<td align='center'><span class='badge badge-info'>Chờ duyệt</span></td>";
						if(levelIdUser == 1) {
							htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.srId + "' class='action edit btn btn-primary-outline' >\
							<i class='fa fa-pencil-square-o' aria-hidden='true' title='Chi tiết'></i></button>";
							htmlDOM += "<button data-target='#modal-command-type-approval' data-id='"+ jsonItem.srId + "' class='action approval btn btn-primary-outline' >\
							<i class='fa fa-rocket' aria-hidden='true' title='Tác động quyền'></i></button>";
							htmlDOM += "<a class='action btn btn-primary-outline cancel' id='"+jsonItem.srId+"' href='/vasonline/service-request/cancel/"+jsonItem.srId+"'>\
							<i class='fa fa-close' title='Huỷ đơn'></i></a></td></tr>";
						} else {
							htmlDOM += "</td></tr>";
						}
						break;
					case 2:
						htmlDOM += "<td align='center'><button class='status_sr btn btn-primary-outline' data-id='"+ jsonItem.description + "' title='Xem lời nhắn'><span class='badge badge-success'>Đã duyệt</span></button></td>";
						if(levelIdUser == brandLevelId || brandLevelId == 0) {
							htmlDOM += "<td align='center'><button data-target='#modal-command-type-activate' data-id='"+ jsonItem.srId + "' class='action activate btn btn-primary-outline' >\
								<i class='fa fa-bolt' aria-hidden='true' title='Kích hoạt'></i></button>";
						}
						if(levelIdUser == 1){
							htmlDOM += "<a class='action btn btn-primary-outline cancel' id='"+jsonItem.srId+"' href='/vasonline/service-request/cancel/"+jsonItem.srId+"'>\
								<i class='fa fa-close' title='Huỷ đơn'></i></a>";
						}
						else{
							htmlDOM += "</td></tr>";
						}
						break;
					case 3:
						htmlDOM += "<td align='center'><button class='status_sr btn btn-primary-outline' data-id='"+ jsonItem.description + "' title='Xem lời nhắn'><span class='badge badge-danger'>Từ chối</span></button>";
						if(levelIdUser == 1){
							htmlDOM += "<a class='action btn btn-primary-outline cancel' id='"+jsonItem.srId+"' href='/vasonline/service-request/cancel/"+jsonItem.srId+"'>\
								<i class='fa fa-close' title='Huỷ đơn'></i></a>";
						}
						else{
							htmlDOM += "</td></tr>";
						}
						break;
					case 4:
						htmlDOM += "<td align='center'><button class='status_sr btn btn-primary-outline' data-id='"+ jsonItem.description + "' title='Xem lời nhắn'><span class='badge badge-warning'>Yêu cầu cập nhật</span></button></td>";
						htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.srId + "' class='action edit btn btn-primary-outline' >\
							<i class='fa fa-pencil-square-o' aria-hidden='true'></i></button>";
						if(levelIdUser == brandLevelId || brandLevelId == 0) {
							htmlDOM += "<button id='adApproval' data-target='#modal-command-type-approval' data-id='"+ jsonItem.srId + "' class='action approval btn btn-primary-outline' >\
								<i class='fa fa-rocket' aria-hidden='true' title='Tác động quyền'></i></button></td></tr>";
						}
						if(levelIdUser == 2 || levelIdUser == 3 || levelIdUser == 4 || levelIdUser == 1) {
							htmlDOM += "<a class='action accept btn btn-primary-outline' id='"+jsonItem.srId+"' href='/vasonline/service-request/accept/"+jsonItem.srId+"'>\
							<i class='fa fa-check-square-o' title='Chuyển duyệt'></i></a>"
						}
						if(levelIdUser == 1){
							htmlDOM += "<a class='action btn btn-primary-outline cancel' id='"+jsonItem.srId+"' href='/vasonline/service-request/cancel/"+jsonItem.srId+"'>\
								<i class='fa fa-close' title='Huỷ đơn'></i></a>";
						}
						else{
							htmlDOM += "</td></tr>";
						}
						break;
					case 5:
						let count = jsonItem.quantity - jsonItem.remainingQuantity;
						htmlDOM += "<td align='center'><button class='list_act btn btn-primary-outline' data-id='"+ jsonItem.srId + "' title='Chi tiết'><span class='badge badge-primary'>Đã chuyển kích hoạt</span></button></br>SL: "+count+"</td>";
						if(jsonItem.remainingQuantity > 0){
							if(levelIdUser == brandLevelId || brandLevelId == 0) {
								htmlDOM += "<td align='center'><button data-target='#modal-command-type-activate' data-id='"+ jsonItem.srId + "' class='action activate btn btn-primary-outline' >\
									<i class='fa fa-bolt' aria-hidden='true' title='Kích hoạt'></i></button></td></tr>";
							}
						}
						break;
					case 6:
						htmlDOM += "<td align='center'><button class='status_sr btn btn-primary-outline' data-id='"+ jsonItem.description + "' title='Xem lời nhắn'><span class='badge badge-danger'>Đã hủy</span></button>";
						// htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.srId + "' class='action edit btn btn-primary-outline' >\
						// 	<i class='fa fa-pencil-square-o' aria-hidden='true' title='Chi tiết'></i></button></td>";
					default:
					console.log('Error');
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

$(document).on("click", ".delete", function(event) {
	event.preventDefault();
	var id = $(this).attr('id');
	var r = confirm("Bạn có muốn xóa yêu cầu dịch vụ mã: " + id + "?");
	if (r) {
		window.location = $(this).attr('href');
	}
});

$(document).on("click", ".accept", function(event) {
	event.preventDefault();
	var id = $(this).attr('id');
	var r = confirm("Bạn có muốn chuyển duyệt yêu cầu dịch vụ mã: " + id + "?");
	if (r) {
		window.location = $(this).attr('href');
	}
});

$(document).on("click", ".cancel", function(e){
	e.preventDefault();
	var r = confirm("Bạn muốn hủy đăng ký dịch vụ?");
	if (r) {
		window.location = $(this).attr('href');

	}
});

$(document).on("click", ".approve", function(event) {
	event.preventDefault();
	var id = $(this).attr('id');
	var r = confirm("Bạn có muốn duyệt yêu cầu dịch vụ ID: " + id + "?");
	if (r) {
		window.location = $(this).attr('href');
	}
});


$("#agency_area").on("change", function (e) {
	let value = Array.from(e.target.selectedOptions, option => option.value);
	if(value != '') {
		if(value == 'all'){
			var linkCustomer = "/vasonline/get-allcustomer";
			$.ajax({
				url: linkCustomer,
				method: 'GET',
				success: function(data) {
					var str ='';
					str +=' <option>-- Chọn khách hàng --</option>';
					for (let i = 0; i < data.length ; i++) {
						str +=' <option value= "'+data[i].id+'" >'+data[i].name+'</option>'
					}
					$('#customer').html(str);
				},
				error: function(request) {
					alert("The request failed: " + request.responseText);
				}
			});
		}
		else {
			var linkCustomer = "/vasonline/get-customer-area?areaId=" + value.toString();
			$.ajax({
				url: linkCustomer,
				method: 'GET',
				success: function (data) {
					var str = '';
					str += ' <option>-- Chọn khách hàng --</option>';
					for (let i = 0; i < data.length; i++) {
						str += ' <option value= "' + data[i].id + '" >' + data[i].name + '</option>'
					}
					$('#customer').html(str);
				},
				error: function (request) {
					alert("The request failed: " + request.responseText);
				}
			});
		}
	}
	else {
		$('#customer').html('');
	}
});

$("#services").on("change", function (e) {
	let value = Array.from(e.target.selectedOptions, option => option.value);
	var linkPost = "/vasonline/brand-group-list?idService="+ value.toString();

	$.ajax({
		url: linkPost,
		method: 'GET',
		success: function(data) {

			var str ='';
			str +=' <option>-- Chọn nhóm gói cước --</option>'
			for (let i = 0; i < data.length ; i++) {
				str +=' <option value= "'+data[i].id+'" >'+data[i].groupName+'</option>'
			}
			$('#package_group').html(str);

		},
		error: function(request) {
			alert("The request failed: " + request.responseText);
		}
	});
});

$("#package_group").on("change", function (e) {
	let value = Array.from(e.target.selectedOptions, option => option.value);
	var linkPost = "/vasonline/brand-list?idBrand="+ value.toString();

	$.ajax({
		url: linkPost,
		method: 'GET',
		success: function(data) {

			var str ='';
			str +=' <option>-- Chọn gói cước --</option>';
			for (let i = 0; i < data.length ; i++) {
				str +=' <option value= "'+data[i].id+'" >'+ data[i].brandId +'-'+data[i].brandName+'</option>'
			}
			$('#package').html(str);

		},
		error: function(request) {
			alert("The request failed: " + request.responseText);
		}
	});
});

function messageNotAuthorized(){
	toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
}

function messageSystemError(){
	toastr.error('Lỗi hệ thống!');
}

//show modal create
$("#doNew").on('click',function () {
	$.ajax({
		url: '/vasonline/service-request/create/',
		type: 'GET',
		success:function(data){
			$('#create_table').html(data);
			$('#modal-command-type-create').modal('toggle');
		},
		error: function(jqXHR, exception){
			if(jqXHR.status == 403){
				messageNotAuthorized();
			}
			else{
				messageSystemError()
			}
			console.log(jqXHR, exception);
		}
	});
});

//show modal edit
$(document).on('click',".edit",function () {
	let requestId = $(this).data('id');
	$.ajax({
		url: '/vasonline/service-request/edit/' + requestId,
		type: 'GET',
		success:function(data){
			$('#edit_table').html(data);
			$('#modal-command-type-edit').modal('toggle');
		},
		error: function(jqXHR, exception){
			if(jqXHR.status == 403){
				messageNotAuthorized();
			}
			else{
				messageSystemError()
			}
			console.log(jqXHR, exception);
		}
	});
});

//show modal approval
$(document).on('click',".approval",function () {
	let requestId = $(this).data('id');
	$.ajax({
		url: '/vasonline/service-request/approval/' + requestId,
		type: 'GET',
		success:function(data){
			$('#approval_table').html(data);
			$('#modal-command-type-approval').modal('toggle');
		},
		error: function(jqXHR, exception){
			if(jqXHR.status == 403){
				messageNotAuthorized();
			}
			else{
				messageSystemError()
			}
			console.log(jqXHR, exception);
		}
	});
});

//show modal description
$(document).on('click',".status_sr",function () {
	let description = $(this).data('id');
	if(description !== 'undefined'){
		$('#desc_table').html(description);
	}
	$('#modal-command-type-desc').modal('toggle');
});

//show modal activate
$(document).on('click',".activate",function () {
	let requestId = $(this).data('id');
	$.ajax({
		url: '/vasonline/service-request/activate/' + requestId,
		type: 'GET',
		success:function(data){
			$('#activate_table').html(data);
			$('#modal-command-type-activate').modal('toggle');
		},
		error: function(jqXHR, exception){
			if(jqXHR.status == 403){
				messageNotAuthorized();
			}
			else{
				messageSystemError()
			}
			console.log(jqXHR, exception);
		}
	});
});

$(document).on('click','.external',function () {
	let customerName = $(this).data('id');
	localStorage.removeItem("name");
	localStorage.setItem("name", customerName);
	window.location.href = "customer";
});

$(document).on('click','.externalBr',function () {
	let brName = $(this).data('id');
	localStorage.removeItem("nameBrand");
	localStorage.setItem("nameBrand", brName);
	window.location.href = "brand";
});

//show list activate
$(document).on('click',".list_act",function () {
	let requestId = $(this).data('id');
	$.ajax({
		url: '/vasonline/service-request/list-active/' + requestId,
		type: 'GET',
		success:function(data){
			$('#list_act_table').html(data);
			$('#modal-command-type-list-act').modal('toggle');
		},
		error: function(jqXHR, exception){
			if(jqXHR.status == 403){
				messageNotAuthorized();
			}
			else{
				messageSystemError()
			}
			console.log(jqXHR, exception);
		}
	});
});

function numberWithCommas(x) {
	var parts = x.toString().split(".");
	parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	return parts.join(".");
}

$(function() {
    // $("#doSearch").click();
	var delayInMilliseconds = 500; //

	setTimeout(function() {
		//your code to be executed after delayInMilliseconds second
		$("#doSearch").click();
	}, delayInMilliseconds);
});