let listPrivileges;
//call api
/*const origin = window.location.origin;*/
$(document).on("click", "#grantAuth", function() {
	$.ajax({
		async: false,
		type: 'GET',
		url: origin + "/vasonline/admin/get/group-privileges/" + $("#groupId").val(),
		success: function(data) {
			loadPrivileges(data);
		}
	});
	let checkBox = false;
	let j = 0;
	let k = 0;
	checkBoxes.each(function(index) {
		let $tr = $('.form-check-input.group-authen-checkbox').closest('tr');
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
		let checkAll = $('#group-check-all');
		checkAll[0].checked = true;
	}
})

//when hide modal, reset all button
$('#authenGroupModal').on('hide.bs.modal', function() {
	const createList = $('.btn.btn-primary-outline.group-authen-create.Selected');
	const editList = $('.btn.btn-primary-outline.group-authen-edit.Selected');
	const deleteList = $('.btn.btn-primary-outline.group-authen-delete.Selected');
	const viewList = $('.btn.btn-primary-outline.group-authen-view.Selected');
	const executeList = $('.btn.btn-primary-outline.authen-execute.Selected');
	const allCheckBoxes = $('.group-authen-checkbox');
	const checkBoxAll = $('#group-check-all');
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
	if($('#group-check-all').is(":checked")){
		isChecked = true;
	}
	else{
		isChecked = false;
	}
	return isChecked;
}

let checkBoxes = $('.group-authen-checkbox');
$('#group-check-all').on('click', function() {
	let ischk = formatChecked();
	if($('#group-check-all').is(":checked")){
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
$('.form-check-input.group-authen-checkbox').on('click', function() {
	let checkBoxStatus = this.checked;	//get check box status
	let $tr = $(this).closest('tr');	//get closest tr
	let $td = $tr[0].cells[2].children;	//get td that contain 4 buttons
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
$('.btn.btn-primary-outline.group-authen-create').on('click', function() {
	if (!$(this).hasClass('Selected')) {
		$(this).addClass('Selected').css('background-color', '#3C8DBC')
	} else {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	}
})
$('.btn.btn-primary-outline.group-authen-edit').on('click', function() {
	if (!$(this).hasClass('Selected')) {
		$(this).addClass('Selected').css('background-color', '#3C8DBC')
	} else {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	}
})
$('.btn.btn-primary-outline.group-authen-delete').on('click', function() {
	if (!$(this).hasClass('Selected')) {
		$(this).addClass('Selected').css('background-color', '#3C8DBC')
	} else {
		$(this).removeClass('Selected').css('background-color', '#b3b3b3');
	}
})
$('.btn.btn-primary-outline.group-authen-view').on('click', function() {
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
$('#GroupSavePrivileges').on('click', function() {
	const cl = $('.btn.btn-primary-outline.group-authen-create.Selected');
	const el = $('.btn.btn-primary-outline.group-authen-edit.Selected');
	const dl = $('.btn.btn-primary-outline.group-authen-delete.Selected');
	const vl = $('.btn.btn-primary-outline.group-authen-view.Selected');
	const exel = $('.btn.btn-primary-outline.authen-execute.Selected');
	const groupId = $("#groupId").val();
	console.log(groupId)
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
	listPri.push(groupId);
	$.ajax({
		async: false,
		type: 'POST',
		url: origin + "/vasonline/admin/group/save/privileges",
		contentType: 'application/json',
		headers: { "X-CSRF-TOKEN": $("input[name='_csrf']").val() },
		data: JSON.stringify(listPri),
		success: function(data) {
			console.log(data)
			$('#authenGroupModal').modal('hide');
			alert('Cập nhật quyền thành công');
		},
		error: function (data, error) {
			alert("Đã xảy ra lỗi");
		}
	});
})