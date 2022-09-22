const origin = window.location.origin;
let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
	pageSize = $('select[name=selector] option').filter(':selected').val();
	$.ajax({
		async: false,
		type: 'GET',
		url: origin + "/vasonline/admin/user/search?username=" + $('#username').val() + "&fullname=" + $('#fullname').val() + "\
		&email=" + $('#email').val() + "&phone=" + $('#phone').val() +"&groupId=" + $('#group-id').val() + "&levelId=" + $('#level-id').val() +"&areaId=" + $('#areaIdSearch').val() +"&agencyId=" + $('#agencyIdSearch').val() +"\
		&page=" + pageNumber + "&size=" + pageSize,
		dataType: 'json',
		success: function(data, responseText, status) {
			requestData = Object.keys(data);
			// requestData.sort();
			let htmlDOM = "<tbody>";
			requestData.forEach((item) => {
				let jsonItem = JSON.parse(item);
				htmlDOM += "<tr><td>" + jsonItem.username + "</td><td>" + jsonItem.fullname + "</td><td>" + jsonItem.phone + "</td><td>" + jsonItem.email + "</td><td>" + jsonItem.levelUsers + "</td><td>" + jsonItem.areaId + "</td>";
				let checkJsonStatus = jsonItem.status;
				if (checkJsonStatus) {
					htmlDOM += "<td align='center'><span class='badge badge-success'>Active</span></td>";
				} else {
					htmlDOM += "<td align='center'><span class='badge badge-danger'>De-active</span></td>";
				}
				htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.username + "' class='action edit btn btn-primary-outline' >\
				<i class='fa fa-pencil' aria-hidden='true'></i></button>\
				<a class='btn btn-primary-outline view' data-id='"+ jsonItem.username + "' data-toggle='modal' data-target='#userModal'> <i\
				class='fa fa-eye'></i></a> <a class='btn btn-primary-outline delete' id='"+ jsonItem.username + "'\
				href='/vasonline/admin/user-management/delete/"+ jsonItem.username + "'><i class='fa fa-trash-o'></i></a> <a class='btn btn-primary-outline authentication'\
				name='"+ jsonItem.username + "' data-toggle='modal' data-target='#authenModal'><i class='fa fa-key'></i></a></td></tr>"
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
			window.location.replace(window.location.origin+'/vasonline/login?message=error');
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
	let eachUser = JSON.parse(item);
	if (eachUser.username == selectedUserId) {
		$(".modal-fullname").text(eachUser.fullname);
		$(".modal-email").text(eachUser.email);
		$(".modal-phone").text(eachUser.phone);
		$(".modal-fax").text(eachUser.fax);
		$(".modal-address").text(eachUser.address);
		$(".modal-createBy").text(eachUser.createBy);
		$(".modal-createDate").text(eachUser.createDate);
		$(".modal-lastLogin").text(eachUser.lastLoginDate);
		$(".modal-userLevel").text(eachUser.levelUsers);
		if (eachUser.status) {
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
	var r = confirm("Xoá tài khoản " + id + "?");
	if (r) {
		window.location = $(this).attr('href');
	}
});

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

function messageNotAuthorized(){
	toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
}

function messageSystemError(){
	toastr.error('Lỗi hệ thống!');
}

//show modal create
$("#doNew").on('click',function () {
	$.ajax({
		url: '/vasonline/admin/user-management/create/',
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
		url: '/vasonline/admin/user-management/edit/' + username,
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