const origin = window.location.origin;
$(document).ready(function() {
	$('#sidebarCollapse').on('click', function() {
		$('#sidebar').toggleClass('active');
	});
});

// function gọi vào app lấy data
function getData() {
	// lấy dữ liệu người dùng nhập
	let username = $('.select2-search__field').val();
	// có dữ liệu
	if (username != '') {
		// get method 
		$.get(origin + "/vasonline/admin/load/user?username=" + username, function(data) {
			// lấy các option đang có để so sánh sau
			let options = $('#group-members option')
			// mỗi 1 data trả về
			$(Object.keys(data)).each(function(index) {
				// với mỗi option
				options.each(function() {
					// nếu không trùng(check cho vui)
					if ((Object.keys(data)[index]) != (options.val())) {
						// xoá option trùng
						$("#group-members [value='" + Object.keys(data)[index] + "']").remove();
						// thêm option mới
						$('#group-members').append(
							'<option value="' + Object.keys(data)[index] + '">' + Object.keys(data)[index] + '</option>'
						)
						//thoát vòng lặp
						return false;
					}
				})
			});
			// đóng và mở lại select box để refresh option mới
			$('#group-members').select2('close')
			$('#group-members').select2('open')
		})
	}
}

// khai báo đối tượng input là select 2 search field cho vui
var $input = $('.select2-search__field');
var typingTimer;                // đối tượng để setTimeout delay
var doneTypingInterval = 1000;  // thời gian delay trước khi user gõ xong để query tìm kiếm

// bắt sự kiện khi click vào ô select
$('#group-members').on('select2:open', function() {
	// set lại giá trị input do select2-search__field giờ mới được gen ra khi click vào ô select
	$input = $('.select2-search__field');
	// sự kiện on keyup khi người dùng ngừng gõ
	$input.on('keyup', function() {
		// làm mới lần cuối đối tượng typingTimer nếu người dùng ngừng gõ
		clearTimeout(typingTimer);
		// tạo 1 typingTimer và set sẽ gọi getData() sau thời gian = doneTypingInterval
		typingTimer = setTimeout(getData, doneTypingInterval);
	});

	// sự kiện on keydown mỗi khi người dùng gõ
	$input.on('keydown', function() {
		// xoá toàn bộ typingTimer đã được setTimeout
		clearTimeout(typingTimer);
	});
})