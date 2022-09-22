const origin = window.location.origin;
let pageNumber = $('#currentPage').text();
let pageSize = $('select[name=selector] option').filter(':selected').val();
let totalPage = 1;

$('#doSearch').on('click', function() {
	pageSize = $('select[name=selector] option').filter(':selected').val();
	$.ajax({
		async: false,
		type: 'GET',
		url: origin + "/vasonline/admin/group/search?groupId=" + $('#group-id').val() + "&groupName=" + $('#group-name').val() + "&groupFather=" + $('#group-father').val() + "&page=" + pageNumber + "&size=" + pageSize,
		success: function(data) {
			let requestGroupData = Object.keys(data);
			// requestGroupData.sort();
			let htmlDOM = "<tbody>";
			requestGroupData.forEach((item) => {
				let jsonItem = JSON.parse(item);
				htmlDOM += "<tr><td><b>" + jsonItem.groupId + "</b></td><td>" + jsonItem.groupName + "</td><td>" + jsonItem.groupFather + "</td><td align='center'>\
				<a class='btn btn-primary-outline' id='edit' href='/vasonline/admin/group-management/edit/" + jsonItem.groupId + "'><i class='fa fa-pencil' ></i >\
				<a class='btn btn-primary-outline delete' id='group-"+ jsonItem.groupId + "' href='/vasonline/admin/group-management/delete/" + jsonItem.groupId + "'><i class='fa fa-trash-o' ></i >\
				<a class='btn btn-primary-outline authentication'\
				name='"+ jsonItem.groupId + "' data-toggle='modal' data-target='#authenGroupModal'><i class='fa fa-key'></i></a></td></tr > ";
			})
			htmlDOM += "</tbody>";
			document.getElementById("tableBody").innerHTML = htmlDOM;
			totalPage = Object.values(data)[0];
			document.getElementById("currentPage").innerHTML = pageNumber;
			document.getElementById("totalPage").innerHTML = "(Tổng số " + totalPage + " trang), Số bản ghi&nbsp";
		}
	});
})

$(document).on("click", ".delete", function(event) {
	event.preventDefault();
	var id = $(this).attr('id');
	var r = confirm("Xoá nhóm " + id + "?");
	if (r) {
		window.location = $(this).attr('href');
	}
});

$(document).on("click", ".authentication", function() {
	$(".modal-content #groupIdPQ").text($(this).attr('name'));
});

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

function messageNotAuthorized(){
	toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
}

function messageSystemError(){
	toastr.error('Lỗi hệ thống!');
}

//show modal create
$("#doNew").on('click',function () {
	$.ajax({
		url: '/vasonline/admin/group-management/create/',
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