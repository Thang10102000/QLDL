let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;
let requestData;

$('#doSearch').on('click', function() {
	pageSize = $('select[name=selector] option').filter(':selected').val();
	$.ajax({
		async: false,
		type: 'GET',
		url: origin + "/vasonline/admin/user/group?username=" + $('#username').val() + "&groupId=" + $('#groupId').val() + "&page=" + pageNumber + "&size=" + pageSize,
		dataType: 'json',
		success: function(data, responseText, status) {
			requestData = Object.keys(data);
			requestData.sort();
			let htmlDOM = "<tbody>";
			requestData.forEach((item) => {
				let jsonItem2 = JSON.parse(item);
				let jsonItem = jsonItem2.usersGU;
				if(jsonItem)
				{
					htmlDOM += "<tr><td>" + jsonItem.username + "</td><td>" + jsonItem.fullname + "</td><td>" + jsonItem.address + "</td><td>" + jsonItem.email + "</td><td>" + jsonItem.phone + "</td><td>" + jsonItem.fax + "</td>";
					let checkJsonStatus = jsonItem.status;
					if (checkJsonStatus) {
						htmlDOM += "<td align='center'><span class='badge badge-success'>Active</span></td>";
					} else {
						htmlDOM += "<td align='center'><span class='badge badge-danger'>De-active</span></td>";
					}
					htmlDOM += "<td align='center'><button data-target='#modal-command-type-edit' data-id='"+ jsonItem.username + "' class='action edit btn btn-primary-outline' >\
					<i class='fa fa-pencil' aria-hidden='true'></i></button> <a class='btn btn-primary-outline view' data-id='"+ jsonItem.username + "' data-toggle='modal' data-target='#userModal'> <i\
					class='fa fa-eye'></i></a> <a class='btn btn-primary-outline delete' id='"+ jsonItem.username + "'\
					href='/vasonline/admin/user/remove/"+ jsonItem2.guId + "'><i class='fa fa-trash-o'></i></a></td></tr>"
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
		}, error: function() {
			//window.location.replace(window.location.origin + '/login?message=max_session');
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
	if (eachUser.usersGU.username == selectedUserId) {
		$(".modal-fullname").text(eachUser.usersGU.fullname);
		$(".modal-email").text(eachUser.usersGU.email);
		$(".modal-phone").text(eachUser.usersGU.phone);
		$(".modal-fax").text(eachUser.usersGU.fax);
		$(".modal-address").text(eachUser.usersGU.address);
		$(".modal-createBy").text(eachUser.usersGU.createBy);
		$(".modal-createDate").text(eachUser.usersGU.createDate);
		$(".modal-lastLogin").text(eachUser.usersGU.lastLoginDate);
		$(".modal-userLevel").text(eachUser.usersGU.levelUsers);
		if (eachUser.usersGU.status) {
			$(".modal-userStatus").text('Active');
		} else {
			$(".modal-userStatus").text('De-active');
		}
	}
}

$(document).on("click", ".view", function() {
	selectedUserId = $(this).data('id');
	$(".modal-content #userId").text(selectedUserId);
	requestData.forEach(findData);
})

$(document).on("click", ".authentication", function() {
	$(".modal-content #userIdPQ").text($(this).attr('name'));
});

$(document).on("click", ".delete", function(event) {
	event.preventDefault();
	var id = $(this).attr('id');
	var r = confirm("Bỏ tài khoản " + id + " khỏi nhóm ?");
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
			console.log(jqXHR, exception);
		}
	});
})