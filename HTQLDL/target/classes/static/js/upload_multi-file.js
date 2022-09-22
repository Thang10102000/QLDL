var filelist = new Array();
updateList = function () {
    var input = document.getElementById('fileUploader');
    var output = document.getElementById('divFiles');

    var html = "";
    for (var i = 0; i < input.files.length; ++i) {
        //filelist[i]=input.files.item(i).name;
        filelist.push(input.files.item(i).name);
    }
    var count = 0;
    for (var i = 0; i < filelist.length; i++){
        count +=1;
        html += "<span>" + filelist[i] + "</span><br>"
    }
    output.innerHTML = html;
}