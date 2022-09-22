$(document).ready(function () {
    var url = new URL(window.location);
    var messageNew = url.searchParams.get("create");
    if (messageNew != null) {
        alert(messageNew);
        window.location.href = "information-agency";
    }
    $("#birthday").datepicker({
        format: "dd/mm/yyyy",
        todayHighlight: true,
        endDate:'today'
    });
    $("#bankName , #bankAccountNo, #bankBranch, #bankAddress").keydown(function () {
        $("#newBankNameMessage").text("");
        $("#newBankAccountNoMessage").text("");
        $("#newBankBranchMessage").text("");
        $("#newBankAddressNoMessage").text("");
    })
    $("#agencyFullName , #phoneNumber,  #agencyEmail").keydown(function () {
        $("#phoneNumberMessage").text("");
        $("#fullNameMessage").text("");
        $("#emailMessage").text("");
    });
    $("#username , #email, #password, #re-password, #phone, #fax, #fullname, #address, #levels, #groups").keydown(function () {
        $("#usernameMessage").text("");
        $("#emailUsernameMessage").text("");
        $("#passwordMessage").text("");
        $("#re-passwordMessage").text("");
        $("#phoneMessage").text("");
        $("#faxMessage").text("");
        $("#fullnameUserMessage").text("");
        $("#addressMessage").text("");
        $("#levelsMessage").text("");
        $("#groupsMessage").text("");
    });
});
var format = /[`!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
$("#doNewBank").on('click', function () {
    var format = /[`!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
    if (format.test($("#bankName").val())) {
        $("#newBankNameMessage").text("Tên ngân hàng chứa ký tự đặc biệt");
        $("#newBankName").focus();
        return false;
    }
    if ($("#bankAccountNo").val() === '') {
        $("#newBankAccountNoMessage").text("Bạn chưa nhập số tài khoản");
        $("#newBankAccountNo").focus();
        return false;
    }
    if ($("#bankAccountNo").val() * 1 != $("#bankAccountNo").val()) {
        $("#newBankAccountNoMessage").text( "Số tài khoản không đúng định dạng");
        $("#newBankAccountNo").focus();
        return false;
    }
    if ($("#bankName").val() === '') {
        $("#newBankNameMessage").text("Bạn chưa nhập tên ngân hàng");
        $("#newBankName").focus();
        return false;
    }
    if ($("#bankBranch").val() === '') {
        $("#newBankBranchMessage").text("Bạn chưa nhập chi nhánh");
        $("#newBankBranch").focus();
        return false;
    }
    if ($("#bankAddress").val() === '') {
        $("#newBankAddressNoMessage").text("Bạn chưa nhập địa chỉ");
        $("#newBankAddress").focus();
        return false;
    }
})

$("#doNewAuthor").on('click', function (e) {
    if (format.test($("#agencyFullName").val())) {
        $("#fullNameMessage").text( "Họ tên chứa ký tự đặc biệt");
        $("#agencyFullName").focus();
        return false;
    }
    if ($("#agencyFullName").val() === '') {
        $("#fullNameMessage").text( "Bạn chưa nhập họ tên");
        $("#agencyFullName").focus();
        return false;
    }
    if ($("#phoneNumber").val() === '') {
        $("#phoneNumberMessage").text( "Bạn chưa nhập số thoại");
        $("#phoneNumber").focus();
        return false;
    }
    if ($("#phoneNumber").val() * 1 != $("#phoneNumber").val() || $("#phoneNumber").val().length < 9
        || $("#phoneNumber").val().length > 11) {
        $("#phoneNumberMessage").text( "Số diện thoại không đúng định dạng");
        $("#phoneNumber").focus();
        return false;
    }
    if ($("#agencyEmail").val() === '') {
        $("#emailMessage").text("Bạn chưa nhập email");
        $("#agencyEmail").focus();
        return false;
    }
    if (!re.test($("#agencyEmail").val())) {
        $("#emailMessage").text( "Email không đúng định dạng");
        return false;
    }
    return true;
})

$("#doNew").on('click', function () {
    if (format.test($("#username").val())) {
        $("#usernameMessage").text( "Tên đăng nhập chứa ký tự đặc biệt");
        $("#username").focus();
        return false;
    }
    if ($("#username").val() === '') {
        $("#usernameMessage").text( "Bạn chưa nhập username");
        $("#username").focus();
        return false;
    }
    if ($("#password").val() === '') {
        $("#passwordMessage").text( "Bạn chưa nhập mật khẩu");
        $("#birthday").focus();
        return false;
    }
    if ($("#re-password").val() === '') {
        $("#re-passwordMessage").text( "Bạn chưa nhập lại mật khẩu");
        $("#re-password").focus();
        return false;
    }
    if ($("#phone").val() === '') {
        $("#phoneMessage").text( "Bạn chưa nhập số thoại");
        $("#phone").focus();
        return false;
    }
    if ($("#phone").val() * 1 != $("#phone").val() || $("#phone").val().length < 9
        || $("#phone").val().length > 11) {
        $("#phoneMessage").text( "Số diện thoại không đúng định dạng");
        $("#phone").focus();
        return false;
    }
    if ($("#email").val() === '') {
        $("#emailUsernameMessage").text("Bạn chưa nhập email");
        $("#email").focus();
        return false;
    }
    if (!re.test($("#email").val())) {
        $("#emailUsernameMessage").text( "Email không đúng định dạng");
        $("#email").focus();
        return false;
    }
    if ($("#fax").val() === '') {
        $("#faxMessage").text("Bạn chưa nhập số fax");
        $("#fax").focus();
        return false;
    }
    if ($("#fax").val() * 1 != $("#fax").val()){
        $("#faxMessage").text( "Số fax không đúng định dạng");
        $("#fax").focus();
        return false;
    }
    if ($("#fullName").val() === '') {
        $("#fullnameUserMessage").text("Bạn chưa nhập họ và tên");
        $("#fullName").focus();
        return false;
    }
    if ($("#address").val() === '') {
        $("#addressMessage").text("Bạn chưa nhập địa chỉ");
        $("#address").focus();
        return false;
    }
    if ($("#levels").val() === '') {
        $("#levelsMessage").text("Bạn chưa chọn cấp tài khoản");
        $("#levels").focus();
        return false;
    }
    if ($("#groups").val() === '') {
        $("#groupsMessage").text("Bạn chưa chọn nhóm tài khoản");
        $("#groups").focus();
        return false;
    }
    return true;
})

var invalidChars = ["-", "+", "e", ",", "."];
$("#bankAccountNo, #phoneNumber, #phone, #fax").on("keydown", function (e) {
    if (invalidChars.includes(e.key)) {
        e.preventDefault();
    }
});