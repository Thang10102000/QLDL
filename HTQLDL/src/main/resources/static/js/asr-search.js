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
		url: origin + "/vasonline/asr/search?agency=" + $('#agency').val() + "&packages=" + $('#package').val() + "&status=" + $('#status').val() +"\
		&asr_id=" + $('#asr_id').val() + "&createdDateFrom=" + $('#createdDateFrom').val() + "&createdDateTo=" + $('#createdDateTo').val() + "\
		&updatedDateFrom="+$('#updatedDateFrom').val()+"&updatedDateTo="+$('#updatedDateTo').val()+"&page=" + pageNumber + "&size=" + pageSize,
		dataType: 'json',
		success: function(data) {
			requestData = Object.keys(data);
			// requestData.sort();
			let htmlDOM = "<tbody>";
			requestData.forEach((item) => {
				let jsonItem = JSON.parse(item);
				let checkJsonStatus = jsonItem.status;
				htmlDOM += "<tr><td>" + jsonItem.requestId + "</td><td>" + jsonItem.agencyASR + "</td><td>" + jsonItem.brandASR + "</td><td>"
				 + jsonItem.customerAccount + "</td><td>" + jsonItem.totalValue + "</td><td>" + jsonItem.createdDate + "</td>";
				if (jsonItem.updatedDate != null) {
					htmlDOM += "<td>" + jsonItem.updatedDate + "</td>";
				} else {
					htmlDOM += "<td></td>";
				}
				switch (checkJsonStatus) {
					case 0:
						htmlDOM += "<td align='center'><span class='edit badge badge-primary'>Mới tạo</span></td>";
						if(jsonItem.message != null) {
							htmlDOM += "<td align='center'>" + jsonItem.message + "</td>";
						}
						else {
							htmlDOM += "<td align='center'></td>";
						}
						htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.requestId + "' class='action edit btn btn-primary-outline' >\
							<i class='fa fa-pencil-square-o' aria-hidden='true'></i></button>\
							<a class='action delete btn btn-primary-outline' id='"+jsonItem.requestId+"' href='/vasonline/asr/delete/"+jsonItem.requestId+"'>\
							<i class='fa fa-trash-o' title='Xóa'></i></a>";
						if(levelIdUser == 3 || levelIdUser == 4) {
							htmlDOM += "<a class='action accept btn btn-primary-outline' id='"+jsonItem.requestId+"' href='/vasonline/asr/accept/"+jsonItem.requestId+"'>\
							<i class='fa fa-check-square-o' title='Chuyển duyệt'></i></a></td></tr>"
						}
						else {
							htmlDOM += "</td></tr>";
						}
						break;
					case 1:
						htmlDOM += "<td align='center'><span class='badge badge-info'>Chờ duyệt</span></td>";
						htmlDOM += "<td></td>";
						htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.requestId + "' class='action edit btn btn-primary-outline' >\
							<i class='fa fa-pencil-square-o' aria-hidden='true'></i></button>";
						if(jsonItem.brandLevelId == levelIdUser) {
							htmlDOM += "<a class='action approve btn btn-primary-outline' id='"+jsonItem.requestId+"' href='/vasonline/asr/approve/"+jsonItem.requestId+"'>\
							<i class='fa fa-handshake-o' title='Duyệt'></i></a></td></tr>";
						} else {
							htmlDOM += "</td></tr>";
						}
						break;
					case 2:
						htmlDOM += "<td align='center'><span class='badge badge-info'>Đang xử lý</span></td></tr>";
						break;
					case 3:
						htmlDOM += "<td align='center'><span class='badge badge-warning'>Đăng ký không thành công</span></td>";
						htmlDOM += "<td align='center'>" + jsonItem.message + "</td>";
						htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.requestId + "' class='action edit btn btn-primary-outline' >\
							<i class='fa fa-pencil-square-o' aria-hidden='true'></i></button>\
							<a class='action register btn btn-primary-outline' id='"+jsonItem.requestId+"' href='/vasonline/asr/reprocess/"+jsonItem.requestId+"'>\
							<i class='fa fa-registered' title='Đăng ký lại'></i></a></td></tr>";
						break;
					case 4:
						htmlDOM += "<td align='center'><span class='badge badge-danger'>Trừ tiền không thành công</span></td>";
						htmlDOM += "<td align='center'>" + jsonItem.message + "</td>";
						htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.requestId + "' class='action edit btn btn-primary-outline' >\
							<i class='fa fa-pencil-square-o' aria-hidden='true'></i></button>\
							<a class='action charge btn btn-primary-outline' id='"+jsonItem.requestId+"' href='/vasonline/asr/recharge/"+jsonItem.requestId+"'>\
							<i class='fa fa-money' title='Trừ tiền lại'></i></a></td></tr>";
						break;
					case 5:
						htmlDOM += "<td align='center'><span class='badge badge-success'>Thành công</span></td>";
						htmlDOM += "<td></td>";
						htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.requestId + "' class='action edit btn btn-primary-outline' >\
							<i class='fa fa-pencil-square-o' aria-hidden='true'></i></button></td></tr>";
						break;
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
	var r = confirm("Bạn có muốn xóa yêu cầu dịch vụ ID: " + id + "?");
	if (r) {
		window.location = $(this).attr('href');
	}
});

$(document).on("click", ".accept", function(event) {
	event.preventDefault();
	var id = $(this).attr('id');
	var r = confirm("Bạn có muốn chuyển duyệt yêu cầu dịch vụ ID: " + id + "?");
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

$(document).on("click", ".register", function(event) {
	event.preventDefault();
	var id = $(this).attr('id');
	var r = confirm("Bạn có muốn đăng ký lại yêu cầu dịch vụ ID: " + id + "?");
	if (r) {
		window.location = $(this).attr('href');
	}
});

$(document).on("click", ".charge", function(event) {
	event.preventDefault();
	var id = $(this).attr('id');
	var r = confirm("Bạn có muốn trừ cước lại yêu cầu dịch vụ ID: " + id + "?");
	if (r) {
		window.location = $(this).attr('href');
	}
});

if ($("#agency_area").val() != ''){
	var linkAgency = "/vasonline/agencyamkam-list?idAgencyArea="+ $("#agency_area").val();
	$.ajax({
		url: linkAgency,
		method: 'GET',
		success: function(data) {
			var str =' <option value= "" >--Chọn đại lý Am/Kam--</option>';
			for (let i = 0; i < data.length ; i++) {
				str +=' <option value= "'+data[i].id+'" >'+data[i].agencyName+'</option>'
			}
			$('#agency').html(str);
		},
		error: function(request) {
			alert("The request failed: " + request.responseText);
		}
	});
}

$("#agency_area").on("change", function (e) {
	let value = Array.from(e.target.selectedOptions, option => option.value);
	var linkAgency = "/vasonline/agencyamkam-list?idAgencyArea="+ value.toString();
	$.ajax({
		url: linkAgency,
		method: 'GET',
		success: function(data) {

			var str ='';
			for (let i = 0; i < data.length ; i++) {
				str +=' <option value= "'+data[i].id+'" >'+data[i].agencyName+'</option>'
			}
			$('#agency').html(str);

		},
		error: function(request) {
			alert("The request failed: " + request.responseText);
		}
	});
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
			str +=' <option>-- Chọn gói cước --</option>'
			for (let i = 0; i < data.length ; i++) {
				str +=' <option value= "'+data[i].id+'" >'+data[i].brandName+'</option>'
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
		url: '/vasonline/asr/create/',
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
	let username = $(this).data('id');
	$.ajax({
		url: '/vasonline/asr/edit/' + username,
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
})