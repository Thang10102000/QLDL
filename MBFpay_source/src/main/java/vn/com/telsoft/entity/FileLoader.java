package vn.com.telsoft.entity;

import java.io.Serializable;

public class FileLoader implements Serializable {
    private String rowid;
    private String group_code;
    private String tnsname = "\\\"\\(description=\\(address=\\(host=localhost\\)\\(protocol=tcp\\)\\(port=1521\\)\\)\\(connect_data=\\(SERVICE_NAME = app\\)\\)\\)\\\"";
    private String dbusername;
    private String dbpassword;
    private String inputfilepath;
    private String tempfilepath;
    private String backupfilepath;
    private String logpath;
    private String wildcard = "*.txt";
    private String uncompress;
    private String startposition = "14";
    private String endposition = "14";
    private String dateformat = "yyyyMMdd";
    private String truncatenextday;
    private String typeofday;
    private String truncatecurrdate;
    private String partitionformat;
    private String partitiondateformat;
    private String tableinsert = "STORES";
    private String insertfield = "date_id,store_name,address,insert_datetime date \\\"dd/mm/yyyy hh24:mi:ss\\\",status";
    private String delimited = ",";
    private String intervalcommit = "10000";
    private String readbuffers = "0";
    private String bindsize = "512000";
    private String errorallowance = "100000";
    private String directpath = "N";
    private String parrallel = "N";
    private String endcloseby;
    private String skipline;
    private String characterset;
    private String logmessagefields = "Path used;Rows successfully loaded;Rows not loaded due to data errors;Total logical records read;Total logical records rejected;Total logical records discarded;Run began;Run ended;Elapsed time was;CPU time was";
    private String checkmaxcurrloader;
    private String logfiletype;
    private String status;
    private String whencolumn;

    public FileLoader() {
    }

    public FileLoader(String rowid, String group_code, String tnsname, String dbusername, String dbpassword, String inputfilepath, String tempfilepath, String backupfilepath, String logpath, String wildcard, String uncompress, String startposition, String endposition, String dateformat, String truncatenextday, String typeofday, String truncatecurrdate, String partitionformat, String partitiondateformat, String tableinsert, String insertfield, String delimited, String intervalcommit, String readbuffers, String bindsize, String errorallowance, String directpath, String parrallel, String endcloseby, String skipline, String characterset, String logmessagefields, String checkmaxcurrloader, String logfiletype, String status, String whencolumn) {
        this.rowid = rowid;
        this.group_code = group_code;
        this.tnsname = tnsname;
        this.dbusername = dbusername;
        this.dbpassword = dbpassword;
        this.inputfilepath = inputfilepath;
        this.tempfilepath = tempfilepath;
        this.backupfilepath = backupfilepath;
        this.logpath = logpath;
        this.wildcard = wildcard;
        this.uncompress = uncompress;
        this.startposition = startposition;
        this.endposition = endposition;
        this.dateformat = dateformat;
        this.truncatenextday = truncatenextday;
        this.typeofday = typeofday;
        this.truncatecurrdate = truncatecurrdate;
        this.partitionformat = partitionformat;
        this.partitiondateformat = partitiondateformat;
        this.tableinsert = tableinsert;
        this.insertfield = insertfield;
        this.delimited = delimited;
        this.intervalcommit = intervalcommit;
        this.readbuffers = readbuffers;
        this.bindsize = bindsize;
        this.errorallowance = errorallowance;
        this.directpath = directpath;
        this.parrallel = parrallel;
        this.endcloseby = endcloseby;
        this.skipline = skipline;
        this.characterset = characterset;
        this.logmessagefields = logmessagefields;
        this.checkmaxcurrloader = checkmaxcurrloader;
        this.logfiletype = logfiletype;
        this.status = status;
        this.whencolumn = whencolumn;
    }

    public FileLoader(FileLoader ett) {
        this.rowid = ett.rowid;
        this.group_code = ett.group_code;
        this.tnsname = ett.tnsname;
        this.dbusername = ett.dbusername;
        this.dbpassword = ett.dbpassword;
        this.inputfilepath = ett.inputfilepath;
        this.tempfilepath = ett.tempfilepath;
        this.backupfilepath = ett.backupfilepath;
        this.logpath = ett.logpath;
        this.wildcard = ett.wildcard;
        this.uncompress = ett.uncompress;
        this.startposition = ett.startposition;
        this.endposition = ett.endposition;
        this.dateformat = ett.dateformat;
        this.truncatenextday = ett.truncatenextday;
        this.typeofday = ett.typeofday;
        this.truncatecurrdate = ett.truncatecurrdate;
        this.partitionformat = ett.partitionformat;
        this.partitiondateformat = ett.partitiondateformat;
        this.tableinsert = ett.tableinsert;
        this.insertfield = ett.insertfield;
        this.delimited = ett.delimited;
        this.intervalcommit = ett.intervalcommit;
        this.readbuffers = ett.readbuffers;
        this.bindsize = ett.bindsize;
        this.errorallowance = ett.errorallowance;
        this.directpath = ett.directpath;
        this.parrallel = ett.parrallel;
        this.endcloseby = ett.endcloseby;
        this.skipline = ett.skipline;
        this.characterset = ett.characterset;
        this.logmessagefields = ett.logmessagefields;
        this.checkmaxcurrloader = ett.checkmaxcurrloader;
        this.logfiletype = ett.logfiletype;
        this.status = ett.status;
        this.whencolumn = ett.whencolumn;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }

    public String getTnsname() {
        return tnsname;
    }

    public void setTnsname(String tnsname) {
        this.tnsname = tnsname;
    }

    public String getDbusername() {
        return dbusername;
    }

    public void setDbusername(String dbusername) {
        this.dbusername = dbusername;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    public String getInputfilepath() {
        return inputfilepath;
    }

    public void setInputfilepath(String inputfilepath) {
        this.inputfilepath = inputfilepath;
    }

    public String getTempfilepath() {
        return tempfilepath;
    }

    public void setTempfilepath(String tempfilepath) {
        this.tempfilepath = tempfilepath;
    }

    public String getBackupfilepath() {
        return backupfilepath;
    }

    public void setBackupfilepath(String backupfilepath) {
        this.backupfilepath = backupfilepath;
    }

    public String getLogpath() {
        return logpath;
    }

    public void setLogpath(String logpath) {
        this.logpath = logpath;
    }

    public String getWildcard() {
        return wildcard;
    }

    public void setWildcard(String wildcard) {
        this.wildcard = wildcard;
    }

    public String getUncompress() {
        return uncompress;
    }

    public void setUncompress(String uncompress) {
        this.uncompress = uncompress;
    }

    public String getStartposition() {
        return startposition;
    }

    public void setStartposition(String startposition) {
        this.startposition = startposition;
    }

    public String getEndposition() {
        return endposition;
    }

    public void setEndposition(String endposition) {
        this.endposition = endposition;
    }

    public String getDateformat() {
        return dateformat;
    }

    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }

    public String getTruncatenextday() {
        return truncatenextday;
    }

    public void setTruncatenextday(String truncatenextday) {
        this.truncatenextday = truncatenextday;
    }

    public String getTypeofday() {
        return typeofday;
    }

    public void setTypeofday(String typeofday) {
        this.typeofday = typeofday;
    }

    public String getTruncatecurrdate() {
        return truncatecurrdate;
    }

    public void setTruncatecurrdate(String truncatecurrdate) {
        this.truncatecurrdate = truncatecurrdate;
    }

    public String getPartitionformat() {
        return partitionformat;
    }

    public void setPartitionformat(String partitionformat) {
        this.partitionformat = partitionformat;
    }

    public String getPartitiondateformat() {
        return partitiondateformat;
    }

    public void setPartitiondateformat(String partitiondateformat) {
        this.partitiondateformat = partitiondateformat;
    }

    public String getTableinsert() {
        return tableinsert;
    }

    public void setTableinsert(String tableinsert) {
        this.tableinsert = tableinsert;
    }

    public String getInsertfield() {
        return insertfield;
    }

    public void setInsertfield(String insertfield) {
        this.insertfield = insertfield;
    }

    public String getDelimited() {
        return delimited;
    }

    public void setDelimited(String delimited) {
        this.delimited = delimited;
    }

    public String getIntervalcommit() {
        return intervalcommit;
    }

    public void setIntervalcommit(String intervalcommit) {
        this.intervalcommit = intervalcommit;
    }

    public String getReadbuffers() {
        return readbuffers;
    }

    public void setReadbuffers(String readbuffers) {
        this.readbuffers = readbuffers;
    }

    public String getBindsize() {
        return bindsize;
    }

    public void setBindsize(String bindsize) {
        this.bindsize = bindsize;
    }

    public String getErrorallowance() {
        return errorallowance;
    }

    public void setErrorallowance(String errorallowance) {
        this.errorallowance = errorallowance;
    }

    public String getDirectpath() {
        return directpath;
    }

    public void setDirectpath(String directpath) {
        this.directpath = directpath;
    }

    public String getParrallel() {
        return parrallel;
    }

    public void setParrallel(String parrallel) {
        this.parrallel = parrallel;
    }

    public String getEndcloseby() {
        return endcloseby;
    }

    public void setEndcloseby(String endcloseby) {
        this.endcloseby = endcloseby;
    }

    public String getSkipline() {
        return skipline;
    }

    public void setSkipline(String skipline) {
        this.skipline = skipline;
    }

    public String getCharacterset() {
        return characterset;
    }

    public void setCharacterset(String characterset) {
        this.characterset = characterset;
    }

    public String getLogmessagefields() {
        return logmessagefields;
    }

    public void setLogmessagefields(String logmessagefields) {
        this.logmessagefields = logmessagefields;
    }

    public String getCheckmaxcurrloader() {
        return checkmaxcurrloader;
    }

    public void setCheckmaxcurrloader(String checkmaxcurrloader) {
        this.checkmaxcurrloader = checkmaxcurrloader;
    }

    public String getLogfiletype() {
        return logfiletype;
    }

    public void setLogfiletype(String logfiletype) {
        this.logfiletype = logfiletype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWhencolumn() {
        return whencolumn;
    }

    public void setWhencolumn(String whencolumn) {
        this.whencolumn = whencolumn;
    }
}
