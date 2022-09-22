let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
	pageSize = $('select[name=selector] option').filter(':selected').val();
	$.ajax({
		async: false,
		type: 'GET',
		url: origin + "/vasonline/admin/sms/search?shortcode=" + $('#shortcodeSearch').val() + "&status=" + $('#statusSearch').val() + "\
		&startdate=" + $('#startdateSearch').val() + "&enddate=" + $('#enddateSearch').val() + "&page=" + pageNumber + "&size=" + pageSize,
		dataType: 'json',
		success: function(data, responseText, status) {
			requestData = Object.keys(data);
			requestData.sort();
			let htmlDOM = "<tbody>";
			requestData.forEach((item, i) => {
				let jsonItem = JSON.parse(item);
				let counter = i + 1;
				htmlDOM += "<tr><td>" + jsonItem.tempCode + "</td><td>" + jsonItem.content + "</td><td>" + jsonItem.startDate + "</td><td>" + jsonItem.endDate + "</td>";
				let checkJsonStatus = jsonItem.status;
				if (checkJsonStatus) {
					htmlDOM += "<td align='center'><span class='badge badge-success'>Active</span></td>";
				} else {
					htmlDOM += "<td align='center'><span class='badge badge-danger'>De-active</span></td>";
				}
				htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.tempCode + "' class='action edit btn btn-primary-outline' >\
                <i class='fa fa-pencil' aria-hidden='true'></i></button>  <a class='btn btn-primary-outline view' data-id='"+ jsonItem.tempCode + "' data-toggle='modal' data-target='#smsModal'> <i\
				class='fa fa-eye'></i></a> <a class='btn btn-primary-outline delete' id='sms"+ jsonItem.tempCode + "'\
				href='/vasonline/admin/sms-management/delete/"+ jsonItem.tempCode + "'><i class='fa fa-trash-o'></i></a></td></tr>"
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
			window.location.replace(window.location.origin + '/vasonline/login?message=error');
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

let selectedUserId;

function findData(item, index) {
	let eachSms = JSON.parse(item);
	if (eachSms.tempCode == selectedUserId) {
		$(".modal-id").text(eachSms.tempCode);
		$(".modal-status").text(eachSms.status);
		$(".modal-startDate").text(eachSms.startDate);
		$(".modal-endDate").text(eachSms.endDate);
		$(".modal-discription").text(eachSms.description);
		$(".modal-nd").text(eachSms.content);

		if (eachSms.status) {
			$(".modal-userStatus").text('Active');
		} else {
			$(".modal-userStatus").text('De-active');
		}
	}
}

$(document).on("click", ".view", function() {
	selectedUserId = $(this).data('id');
	$(".modal-content #userId").text(selectedUserId);
	requestData.forEach(findData)
})

$(document).on("click", ".authentication", function() {
	$(".modal-content #userIdPQ").text($(this).attr('name'));
});

$(document).on("click", ".delete", function(event) {
	event.preventDefault();
	var id = $(this).attr('id');
	var r = confirm("Xoá mẫu tin nhắn " + id + "?");
	if (r) {
		window.location = $(this).attr('href');
	}
});

//show modal create
$("#doNewSearch").on('click',function () {
	$.ajax({
		url: '/vasonline/admin/sms-management/create',
		type: 'GET',
		success:function(data){
			$('#create_table').html(data);
			$('#modal-command-type-create').modal('toggle');
		},
		error: function (jqXHR,exception) {
			if (jqXHR.status == 403){
				toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
			}else {
				toastr.error('Lỗi hệ thống!');
			}

		}
	});
});

//show modal
$(document).on('click',".edit",function () {
	let id = $(this).data('id');
	$.ajax({
		url: '/vasonline/admin/sms-management/edit/' + id,
		type: 'GET',
		success:function(data){
			$('#edit_table').html(data);
			$('#modal-command-type-edit').modal('toggle');
		},
		error: function (jqXHR,exception) {
			if (jqXHR.status == 403){
				toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
			}else {
				toastr.error('Lỗi hệ thống!');
			}

		}
	});
})

$(document).on("change", "#email", function() {
	pageNumber = 1;
})
$(document).on("change", "#fullname", function() {
	pageNumber = 1;
})
$(document).on("change", "#address", function() {
	pageNumber = 1;
})
$(document).on("change", "#phone", function() {
	pageNumber = 1;
})
$(document).on("change", "#fax", function() {
	pageNumber = 1;
})