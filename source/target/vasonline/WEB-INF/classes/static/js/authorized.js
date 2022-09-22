let listPrivileges;
//call api
$(document).on("click", ".authentication", function() {
	$.ajax({
		async: false,
		type: 'GET',
		url: origin + "/vasonline/admin/get/privileges/" + this.name,
		success: function(data) {
			loadPrivileges(data);
		},
		error: function(jqXHR, exception){
			if(jqXHR.status == 403){
				messageNotAuthorized();
			}
			else{
				messageSystemError();
			}
			console.log(jqXHR, exception);
		}
	});
	let checkBox = false;
	let j = 0;
	let k = 0;
	checkBoxes.each(function(index) {
		let $tr = $('.form-check-input.authen-checkbox').closest('tr');
		let $td = $tr[index].cells[2].children;	//get td that contain 5 buttons
		let i=0;
		for (let item of $td) {
			if ($(item).hasClass('Selected')) {
				i++;
			}
		}
		j++;
		if(i===5){
			checkBoxes[index].checked = !isChecked;
			k++;
		}
	})
	if(k >= j){
		let checkAll = $('#check-all');
		checkAll[0].checked = true;
	}
});

function messageNotAuthorized(){
	toastr.error('Tài khoản không được phân quyền để thực hiện tác vụ!');
}

function messageSystemError(){
	toastr.error('Lỗi hệ thống!');
}

//when hide modal, reset all button
$('#authenModal').on('hide.bs.modal', function() {
	const createList = $('.btn.btn-primary-outline.authen-create.Selected');
	const editList = $('.btn.btn-primary-outline.authen-edit.Selected');
	const deleteList = $('.btn.btn-primary-outline.authen-delete.Selected');
	const viewList = $('.btn.btn-primary-outline.authen-view.Selected');
	const executeList = $('.btn.btn-primary-outline.authen-execute.Selected');
	const allCheckBoxes = $('.authen-checkbox');
	const checkBoxAll = $('#check-all');
	createList.each(function() {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	})
	editList.each(function() {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	})
	deleteList.each(function() {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	})
	viewList.each(function() {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	})
	executeList.each(function() {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	})
	allCheckBoxes.each(function() {
		$(this).prop("checked", false);
	})
	checkBoxAll.prop("checked", false);
	isChecked = false;
})

//click all button by data input
function loadPrivileges(data) {
	for (let item in data) {
		document.getElementById(item).click();
	}
}

//function select - deselect all checkbox
let isChecked = false;
function formatChecked() {
	if($('#check-all').is(":checked")){
		isChecked = true;
	}
	else{
		isChecked = false;
	}
	return isChecked;
}

let checkBoxes = $('.authen-checkbox');
$('#check-all').on('click', function() {
	let ischk = formatChecked();
	if($('#check-all').is(":checked")){
		isChecked = ischk;
		checkBoxes.each(function(index) {
			if (checkBoxes[index].checked == !isChecked) {
				document.getElementById(checkBoxes[index].id).click();
				checkBoxes[index].checked = isChecked;
			}
		});
	}
	else{
		isChecked = ischk;
		checkBoxes.each(function(index) {
			if (checkBoxes[index].checked == !isChecked) {
				document.getElementById(checkBoxes[index].id).click();
				checkBoxes[index].checked = isChecked;
			}
		});
	}

})

//function get all button id by checked box
$('.form-check-input.authen-checkbox').on('click', function() {
	let checkBoxStatus = this.checked;	//get check box status
	let $tr = $(this).closest('tr');	//get closest tr
	let $td = $tr[0].cells[2].children;	//get td that contain 5 buttons
	for (let item of $td) {
		if (checkBoxStatus) {
			if (!$(item).hasClass('Selected')) {
				document.getElementById(item.id).click();	//click to disable button if already on
			}
		} else {
			if ($(item).hasClass('Selected')) {
				document.getElementById(item.id).click();	//click to disable button if it is on
			}
		}
	}
})

//function set button on-off color
$('.btn.btn-primary-outline.authen-create').on('click', function() {
	if (!$(this).hasClass('Selected')) {
		$(this).addClass('Selected').css('background-color', '#3C8DBC')
	} else {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	}
})
$('.btn.btn-primary-outline.authen-edit').on('click', function() {
	if (!$(this).hasClass('Selected')) {
		$(this).addClass('Selected').css('background-color', '#3C8DBC')
	} else {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	}
})
$('.btn.btn-primary-outline.authen-delete').on('click', function() {
	if (!$(this).hasClass('Selected')) {
		$(this).addClass('Selected').css('background-color', '#3C8DBC')
	} else {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	}
})
$('.btn.btn-primary-outline.authen-view').on('click', function() {
	if (!$(this).hasClass('Selected')) {
		$(this).addClass('Selected').css('background-color', '#3C8DBC')
	} else {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	}
})
$('.btn.btn-primary-outline.authen-execute').on('click', function() {
	if (!$(this).hasClass('Selected')) {
		$(this).addClass('Selected').css('background-color', '#3C8DBC')
	} else {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	}
})

//save privileges
$('#SavePrivileges').on('click', function() {
	const cl = $('.btn.btn-primary-outline.authen-create.Selected');
	const el = $('.btn.btn-primary-outline.authen-edit.Selected');
	const dl = $('.btn.btn-primary-outline.authen-delete.Selected');
	const vl = $('.btn.btn-primary-outline.authen-view.Selected');
	const exel = $('.btn.btn-primary-outline.authen-execute.Selected');
	const username = $("#userIdPQ").text();
	console.log(username)
	let listPri = [];
	for (let i of cl) {
		listPri.push(i.id)
	}
	for (let i of el) {
		listPri.push(i.id)
	}
	for (let i of dl) {
		listPri.push(i.id)
	}
	for (let i of vl) {
		listPri.push(i.id)
	}
	for (let i of exel) {
		listPri.push(i.id)
	}
	listPri.push(username);
	$.ajax({
		async: false,
		type: 'POST',
		url: origin + "/vasonline/admin/save/privileges",
		contentType: 'application/json',
		headers: { "X-CSRF-TOKEN": $("input[name='_csrf']").val() },
		data: JSON.stringify(listPri),
		success: function(data) {
			$('#authenModal').modal('hide');
			toastr.success('Thành công');
		},
		error: function (data, error) {
			$('#authenModal').modal('hide');
			toastr.success('Thất bại');
		}
	});
})