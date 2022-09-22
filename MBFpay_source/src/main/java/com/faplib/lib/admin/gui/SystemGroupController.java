//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.faplib.lib.admin.gui;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.lib.TelsoftException;
import com.faplib.lib.ClientMessage.MESSAGE_TYPE;
import com.faplib.lib.admin.gui.data.AppGuiModel;
import com.faplib.lib.admin.gui.data.GroupGuiModel;
import com.faplib.lib.admin.gui.data.ModuleRightGuiModel;
import com.faplib.lib.admin.gui.data.PayAreaGuiModel;
import com.faplib.lib.admin.gui.data.UserGuiModel;
import com.faplib.lib.admin.gui.entity.AppGUI;
import com.faplib.lib.admin.gui.entity.GroupDTL;
import com.faplib.lib.admin.gui.entity.GroupGUI;
import com.faplib.lib.admin.gui.entity.ModuleRightGUI;
import com.faplib.lib.admin.gui.entity.PayAreaGUI;
import com.faplib.lib.admin.gui.entity.UserGUI;
import com.faplib.lib.admin.security.Authorizator;
import com.faplib.lib.admin.security.PolicyProcessor;
import com.faplib.lib.util.ResourceBundleUtil;
import com.faplib.util.StringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.lang.SerializationUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;

@Named
@ViewScoped
public class SystemGroupController extends TSFuncTemplate implements Serializable {
    private List<ModuleRightGUI> mlistModuleRight;
    private List<UserGUI> mlistUser;
    private List<GroupGUI> mlistGroupGUI;
    private List<PayAreaGUI> mlistDistrict;
    private List<PayAreaGUI> mlistBranch;
    private List<AppGUI> mlistAppGUI;
    private List<GroupGUI> mselectedPickGroup;
    private GroupGUI mselectedPickParentGroupGUI;
    private GroupGUI mrecentAddedGroup;
    private GroupGUI mtmpGroupGUI;
    private UserGUI mtmpUserGUI;
    private List<UserGUI> mselectedUser;
    private String mselectedBranch;
    private String mselectedDistrict;
    private TreeNode mtreeGroup;
    private TreeNode mselectedGroupNode;
    private String[] mstrStatus;
    private String[] mstrExpireStatus;
    private int miAction;
    private String mstrUserNewPassword;
    private String mstrUserReNewPassword;
    private String strCheckedGroups;
    private long mstrUserOldStatus;
    private String mstrAppCode;
    private boolean mbIsIncludeChild;
    private boolean mbIsIncludeChildChange;
    private String mstrGroupSearch;
    private DualListModel<UserGUI> mdualListUser;
    private List<UserGUI> mlistSource;
    private List<UserGUI> mselectedUserLazySelected;
    private List<UserGUI> mselectedUserLazy;
    private List<UserGUI> mlistUserGroupSelected;
    private List<UserGUI> mlistUserGroup;
    private AppGuiModel mappGuiModel = new AppGuiModel();
    private GroupGuiModel mgroupGuiModel = new GroupGuiModel();
    private UserGuiModel muserGuiModel = new UserGuiModel();
    private PayAreaGuiModel mpayAreaGuiModel = new PayAreaGuiModel();
    private ModuleRightGuiModel mmoduleRightGuiModel = new ModuleRightGuiModel();
    private String[] mstrTitle;

    public SystemGroupController() throws Exception {
        this.mlistAppGUI = this.mappGuiModel.getListApp();
        this.mstrAppCode = ((AppGUI)this.mlistAppGUI.get(0)).getCode();
        this.mlistGroupGUI = this.mgroupGuiModel.getListAmGroupTree();
        this.mtmpGroupGUI = new GroupGUI();
        this.resetTreeGroup();
        this.setSelectedTofirstGroupTreeNode();
        this.miAction = 1;
        this.mbIsIncludeChild = false;
        this.mbIsIncludeChildChange = false;
        this.mlistSource = this.getSourcePickList();
        this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
        this.mlistUser = this.muserGuiModel.getListAmUser();
        this.mdualListUser = new DualListModel();
        this.mlistModuleRight = new ArrayList();
        this.mlistModuleRight.add(new ModuleRightGUI());
        this.mstrStatus = new String[3];
        this.mstrStatus[0] = ResourceBundleUtil.getCommonObjectAsString("disable");
        this.mstrStatus[1] = ResourceBundleUtil.getCommonObjectAsString("enable");
        this.mstrStatus[2] = ResourceBundleUtil.getCommonObjectAsString("system");
        this.mstrExpireStatus = new String[3];
        this.mstrExpireStatus[0] = ResourceBundleUtil.getModuleObjectAsString("pwd_expire");
        this.mstrExpireStatus[1] = ResourceBundleUtil.getModuleObjectAsString("pwd_not_expire");
        this.mstrExpireStatus[2] = ResourceBundleUtil.getModuleObjectAsString("pwd_never_expire");
        this.mlistDistrict = new ArrayList();
        this.mlistBranch = this.mpayAreaGuiModel.getListBranch();
        this.mstrUserNewPassword = "";
        this.mstrUserOldStatus = -1L;
        this.mstrTitle = new String[4];
        this.mstrTitle[0] = ResourceBundleUtil.getModuleObjectAsString("title_0");
        this.mstrTitle[1] = ResourceBundleUtil.getModuleObjectAsString("title_1");
        this.mstrTitle[2] = ResourceBundleUtil.getModuleObjectAsString("title_2");
        this.mstrTitle[3] = ResourceBundleUtil.getModuleObjectAsString("title_3");
    }

    public String getUserTitle(int input) {
        return this.mstrTitle[input];
    }

    public String getUsername(long userId) {
        Iterator var3 = this.mlistUser.iterator();

        UserGUI userGUI;
        do {
            if (!var3.hasNext()) {
                return "";
            }

            userGUI = (UserGUI)var3.next();
        } while(userGUI.getUser().getUserId() != userId);

        return userGUI.getUser().getUserName();
    }

    public boolean isAllowEditUser(UserGUI user) throws Exception {
        if (!this.isIsAllowUpdate()) {
            return false;
        } else {
            return AdminUser.isSuperAdmin() || user.getUser().getStatus() != 2L;
        }
    }

    public boolean isAllowCopyUser(UserGUI user) throws Exception {
        if (!this.isIsAllowInsert()) {
            return false;
        } else {
            return AdminUser.isSuperAdmin() || user.getUser().getStatus() != 2L;
        }
    }

    public boolean isAllowDeleteUser(UserGUI user) throws Exception {
        if (!this.isIsAllowDelete()) {
            return false;
        } else {
            return AdminUser.isSuperAdmin() || user.getUser().getStatus() != 2L;
        }
    }

    public void onGroupSearchSelect(SelectEvent event) throws Exception {
        String strGroupId = event.getObject().toString().substring(1, event.getObject().toString().indexOf("] "));
        long groupId = Long.parseLong(strGroupId);
        PrimeFaces.current().executeScript("scrollTreeTo('group_" + groupId + "')");
        Iterator var5 = this.mlistGroupGUI.iterator();

        GroupGUI tmpGroup;
        do {
            if (!var5.hasNext()) {
                return;
            }

            tmpGroup = (GroupGUI)var5.next();
        } while(tmpGroup.getGroup().getGroupId() != groupId);

        this.miAction = 2;
        this.clearSelectedNode(this.mtreeGroup);
        this.setSelectedNode(this.mtreeGroup, groupId);
        this.mtmpGroupGUI = tmpGroup;
        this.mselectedGroupNode = new DefaultTreeNode(this.mtmpGroupGUI, (TreeNode)null);
        Iterator var7 = this.mlistGroupGUI.iterator();

        while(var7.hasNext()) {
            GroupGUI tmpGroup2 = (GroupGUI)var7.next();
            if (tmpGroup2.getGroup().getGroupId() == this.mtmpGroupGUI.getGroup().getParentId()) {
                this.mselectedPickParentGroupGUI = tmpGroup2;
                break;
            }
        }

        this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
    }

    public List<String> completeGroupSearch(String strQuery) {
        List<String> returnVal = new ArrayList();
        strQuery = StringUtil.removeSign(strQuery.toUpperCase());
        Iterator var3 = this.mlistGroupGUI.iterator();

        while(var3.hasNext()) {
            GroupGUI tmpGroup = (GroupGUI)var3.next();
            if (StringUtil.removeSign(tmpGroup.getGroup().getName().toUpperCase()).contains(strQuery)) {
                returnVal.add("[" + tmpGroup.getGroup().getGroupId() + "] " + tmpGroup.getGroup().getName());
            }
        }

        return returnVal;
    }

    public void handMoveUser() {
        Iterator var1 = this.mselectedUserLazy.iterator();

        while(var1.hasNext()) {
            UserGUI tmpUser = (UserGUI)var1.next();
            tmpUser.setIsNewMove(true);
        }

        this.mlistUserGroupSelected.addAll(0, this.mselectedUserLazy);
        this.mlistUserGroup.removeAll(this.mselectedUserLazy);
        this.mselectedUserLazy = null;
    }

    public void handMoveUserSelected() throws Exception {
        if (!this.mgroupGuiModel.checkMoveUserGroup(this.mselectedUserLazySelected)) {
            ClientMessage.logPErr("cannot_move_user_has_one_group");
        } else {
            Iterator var1 = this.mselectedUserLazySelected.iterator();

            while(var1.hasNext()) {
                UserGUI tmpUser = (UserGUI)var1.next();
                tmpUser.setIsNewMove(false);
            }

            this.mlistUserGroup.addAll(0, this.mselectedUserLazySelected);
            this.mlistUserGroupSelected.removeAll(this.mselectedUserLazySelected);
            this.mselectedUserLazySelected = null;
        }
    }

    public void onSelectedUser() {
        this.mselectedUserLazySelected = null;
    }

    public void onSelectedUserSelected() {
        this.mselectedUserLazy = null;
    }

    private void buildDualListUserGroup() {
        this.mlistUserGroup = new ArrayList();
        this.mlistUserGroupSelected = new ArrayList();
        if (this.mtmpGroupGUI.getListUser() != null) {
            this.mlistUserGroupSelected.addAll(this.mtmpGroupGUI.getListUser());
        }

        Iterator var2 = this.mlistSource.iterator();

        while(var2.hasNext()) {
            UserGUI tmpUserGUI = (UserGUI)var2.next();
            boolean bIsExistOnTarget = false;
            Iterator var4 = this.mlistUserGroupSelected.iterator();

            while(var4.hasNext()) {
                UserGUI tmp = (UserGUI)var4.next();
                if (tmpUserGUI.getUser().getUserId() == tmp.getUser().getUserId()) {
                    bIsExistOnTarget = true;
                    break;
                }
            }

            if (!bIsExistOnTarget) {
                this.mlistUserGroup.add(new UserGUI(tmpUserGUI));
            }
        }

    }

    private void setSelectedTofirstGroupTreeNode() {
        if (this.mtreeGroup.getChildCount() != 0) {
            ((TreeNode)this.mtreeGroup.getChildren().get(0)).setSelected(true);
            this.mselectedGroupNode = (TreeNode)this.mtreeGroup.getChildren().get(0);
            this.mtmpGroupGUI = (GroupGUI)this.mselectedGroupNode.getData();
        }
    }

    private void buildTreeGroup(TreeNode parent) {
        GroupGUI treeValue = (GroupGUI)parent.getData();
        Iterator var3 = this.mlistGroupGUI.iterator();

        while(true) {
            GroupGUI groupGUI;
            do {
                if (!var3.hasNext()) {
                    return;
                }

                groupGUI = (GroupGUI)var3.next();
            } while(groupGUI.getGroup().getParentId() != treeValue.getGroup().getGroupId());

            TreeNode tmpTN = new DefaultTreeNode(groupGUI, parent);
            if (groupGUI.getGroup().getLevel() <= 2) {
                tmpTN.setExpanded(true);
            }

            if (groupGUI.getGroup().getGroupId() == this.mtmpGroupGUI.getGroup().getGroupId()) {
                tmpTN.setSelected(true);
                this.mselectedGroupNode = tmpTN;
                TreeNode tmp = tmpTN;

                for(int i = 0; i < 10000 && ((TreeNode)tmp).getParent() != null; ++i) {
                    ((TreeNode)tmp).getParent().setExpanded(true);
                    tmp = ((TreeNode)tmp).getParent();
                }
            } else {
                tmpTN.setSelected(false);
            }

            this.buildTreeGroup(tmpTN);
        }
    }

    private void setSelectedNode(TreeNode tnInput, long groupId) {
        Iterator var4 = tnInput.getChildren().iterator();

        TreeNode tn;
        do {
            if (!var4.hasNext()) {
                return;
            }

            tn = (TreeNode)var4.next();
            if (tn.getChildCount() != 0) {
                this.setSelectedNode(tn, groupId);
            }
        } while(((GroupGUI)tn.getData()).getGroup().getGroupId() != groupId);

        TreeNode tmpTn = tn;
        tn.setSelected(true);

        for(int i = 0; i < 10000 && tmpTn.getParent() != null; ++i) {
            tmpTn.getParent().setExpanded(true);
            tmpTn = tmpTn.getParent();
        }

    }

    private void clearSelectedNode(TreeNode tnInput) {
        Iterator var2 = tnInput.getChildren().iterator();

        while(var2.hasNext()) {
            TreeNode tn = (TreeNode)var2.next();
            if (tn.getChildCount() == 0) {
                tn.setSelected(false);
            } else {
                tn.setSelected(false);
                this.clearSelectedNode(tn);
            }
        }

    }

    private void resetTreeGroup() {
        Iterator var1 = this.mlistGroupGUI.iterator();

        while(var1.hasNext()) {
            GroupGUI groupGUI = (GroupGUI)var1.next();
            groupGUI.setListUser((List)null);
        }

        GroupGUI rootTN = new GroupGUI();
        this.mtreeGroup = new DefaultTreeNode(rootTN, (TreeNode)null);
        this.buildTreeGroup(this.mtreeGroup);
    }

    public void onCheckChildGroupUser() throws Exception {
        this.mlistSource = this.getSourcePickList();
        this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
        this.mbIsIncludeChildChange = true;
    }

    public void onPickParentGroup() {
        this.mtmpGroupGUI.getGroup().setParentId(this.mselectedPickParentGroupGUI.getGroup().getGroupId());
    }

    public void onGroupNodeSelect(NodeSelectEvent event) throws Exception {
        this.miAction = 2;
        this.mtmpGroupGUI = (GroupGUI)event.getTreeNode().getData();
        boolean isFound = false;
        Iterator var3 = this.mlistGroupGUI.iterator();

        while(var3.hasNext()) {
            GroupGUI tmpGroup = (GroupGUI)var3.next();
            if (tmpGroup.getGroup().getGroupId() == this.mtmpGroupGUI.getGroup().getParentId()) {
                this.mselectedPickParentGroupGUI = tmpGroup;
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            this.mselectedPickParentGroupGUI = new GroupGUI();
        }

        this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
        this.clearSelectedNode(this.mtreeGroup);
        this.setSelectedNode(this.mtreeGroup, this.mtmpGroupGUI.getGroup().getGroupId());
    }

    public void changeStateAddGroup() throws Exception {
        super.changeStateAdd();
        this.miAction = 2;
        long parentId = this.mtmpGroupGUI.getGroup().getGroupId();
        this.mselectedPickParentGroupGUI = this.mtmpGroupGUI;
        this.selectedIndex = this.mlistGroupGUI.indexOf(this.mtmpGroupGUI);
        this.mtmpGroupGUI = new GroupGUI();
        this.mtmpGroupGUI.getGroup().setGroupId(-1L);
        this.mtmpGroupGUI.getGroup().setParentId(parentId);
        this.mtmpGroupGUI.setListUser(new ArrayList());
        this.buildDualListUserGroup();
        this.buildModuleRight("null");
    }

    public void changeStateCopyGroup() throws Exception {
        super.changeStateCopy();
        this.miAction = 2;
        this.mtmpGroupGUI = (GroupGUI)SerializationUtils.clone(this.mtmpGroupGUI);
        this.buildDualListUserGroup();
        this.buildModuleRight("group");
    }

    public void changeStateEditGroup() throws Exception {
        super.changeStateEdit();
        this.selectedIndex = this.mlistGroupGUI.indexOf(this.mtmpGroupGUI);
        this.mtmpGroupGUI = (GroupGUI)SerializationUtils.clone(this.mtmpGroupGUI);
        this.miAction = 2;
        this.buildDualListUserGroup();
        this.buildModuleRight("group");
    }

    public void changeStateViewGroup() throws Exception {
        this.changeStateEditGroup();
        super.changeStateView();
    }

    public void handDeleteGroup() throws Exception {
        if (this.isIsAllowDelete()) {
            this.mgroupGuiModel.delete(this.mtmpGroupGUI.getGroup().getGroupId());
            this.mlistGroupGUI.remove(this.mtmpGroupGUI);
            this.resetTreeGroup();
            this.setSelectedTofirstGroupTreeNode();
            ClientMessage.logDelete();
        }
    }

    private void handOkGroup() throws Exception {
        if (!this.isADD && !this.isCOPY) {
            if (this.isEDIT) {
                if (!this.getPermission("U")) {
                    return;
                }

                if (this.mtmpGroupGUI.getGroup().getGroupId() == this.mtmpGroupGUI.getGroup().getParentId()) {
                    ClientMessage.logPErr("cannot_assign_parent_group_to_itself");
                    return;
                }

                Iterator var1 = this.getListChildGroup(this.mtmpGroupGUI).iterator();

                while(var1.hasNext()) {
                    GroupGUI group = (GroupGUI)var1.next();
                    if (this.mtmpGroupGUI.getGroup().getParentId() == group.getGroup().getGroupId()) {
                        ClientMessage.logPErr("cannot_assign_parent_group_to_child");
                        return;
                    }
                }

                this.mgroupGuiModel.update(this.mtmpGroupGUI, this.mlistUserGroupSelected, this.mlistModuleRight);
                this.mlistGroupGUI.set(this.selectedIndex, this.mtmpGroupGUI);
                this.mtmpGroupGUI.setListUser(this.mlistUserGroupSelected);
                this.mlistGroupGUI.remove(this.mtmpGroupGUI);
                this.mlistGroupGUI.add(this.mlistGroupGUI.indexOf(this.mselectedPickParentGroupGUI) + 1, this.mtmpGroupGUI);
                this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
                ClientMessage.logUpdate();
            }
        } else {
            if (!this.getPermission("I")) {
                return;
            }

            this.mgroupGuiModel.add(this.mtmpGroupGUI, this.mlistUserGroupSelected, this.mlistModuleRight, this.isADD ? "add" : "copy");
            this.mrecentAddedGroup = this.mtmpGroupGUI;
            this.mtmpGroupGUI.getGroup().setLevel(this.mselectedPickParentGroupGUI.getGroup().getLevel() + 1);
            this.mtmpGroupGUI.setListUser(this.mlistUserGroupSelected);
            this.mlistGroupGUI.add(this.mlistGroupGUI.indexOf(this.mselectedPickParentGroupGUI) + 1, this.mtmpGroupGUI);
            this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
            ClientMessage.logAdd();
            if (this.isADD) {
                this.mtmpGroupGUI = new GroupGUI();
                this.mtmpGroupGUI.getGroup().setParentId(this.mselectedPickParentGroupGUI.getGroup().getGroupId());
                this.mdualListUser = new DualListModel(this.mlistUser, new ArrayList());
                this.buildModuleRight("null");
            }
        }

    }

    private List<GroupGUI> getListChildGroup(GroupGUI group) {
        List<GroupGUI> listReturn = new ArrayList();
        Iterator var3 = this.mlistGroupGUI.iterator();

        while(var3.hasNext()) {
            GroupGUI groupGUI = (GroupGUI)var3.next();
            if (groupGUI.getGroup().getParentId() == group.getGroup().getGroupId()) {
                listReturn.add(groupGUI);
            }
        }

        return listReturn;
    }

    private List<UserGUI> getListUserByGroup(boolean bForceRebuild) throws Exception {
        long groupId = this.mtmpGroupGUI.getGroup().getGroupId();
        if (this.mbIsIncludeChildChange) {
            bForceRebuild = true;
            this.mbIsIncludeChildChange = false;
        }

        Iterator var4 = this.mlistGroupGUI.iterator();

        GroupGUI groupGUI;
        do {
            if (!var4.hasNext()) {
                return new ArrayList();
            }

            groupGUI = (GroupGUI)var4.next();
        } while(groupGUI.getGroup().getGroupId() != groupId);

        if (groupGUI.getListUser() == null || bForceRebuild) {
            groupGUI.setListUser(this.muserGuiModel.getListAmUserByGroupId(groupId, this.mbIsIncludeChild));
        }

        return groupGUI.getListUser();
    }

    private List<UserGUI> getSourcePickList() throws Exception {
        long strGroupId = ((GroupGUI)((TreeNode)this.mtreeGroup.getChildren().get(0)).getData()).getGroup().getGroupId();
        return this.muserGuiModel.getListAmUserByGroupId(strGroupId, true);
    }

    public void onPickGroup() throws Exception {
        //clean
        if (!this.mlistModuleRight.isEmpty()) {
            for(int i = 0; i < this.mlistModuleRight.size(); i++) {
                for(int j = 0; j < this.mlistModuleRight.get(i).getListAccess().size(); j++) {
                    this.mlistModuleRight.get(i).getListAccess().get(j).setAccess(2);
                }
            }
        }

        this.mtmpUserGUI.setListGroup(new ArrayList());
        Iterator var1 = this.mselectedPickGroup.iterator();

        while(var1.hasNext()) {
            GroupGUI tmpGroupGui = (GroupGUI)var1.next();
            this.mtmpUserGUI.getListGroup().add(tmpGroupGui.getGroup());
        }
        // get list access of group
        for(int i = 0; i < this.mtmpUserGUI.getListGroup().size(); i++) {
            List<ModuleRightGUI> moduleRightGUIListTemp = buildModuleRightDialog(this.mtmpUserGUI.getListGroup().get(i).getGroupId(), "group");
            for(int j = 0; j < moduleRightGUIListTemp.size(); j++) {
                for(int k = 0; k < moduleRightGUIListTemp.get(j).getListAccess().size(); k++) {
                    int accessGroup = moduleRightGUIListTemp.get(j).getListAccess().get(k).getAccess();
                    int accessCurrent = this.mlistModuleRight.get(j).getListAccess().get(k).getAccess();

                    if(accessGroup == 1) {
                        this.mlistModuleRight.get(j).getListAccess().get(k).setAccess(1);
                    }
                    else if(accessGroup == 0 &&
                            accessCurrent != 1){
                        this.mlistModuleRight.get(j).getListAccess().get(k).setAccess(0);
                    }
                    else if(accessGroup == 0 &&
                            accessCurrent == 1) {
                        this.mlistModuleRight.get(j).getListAccess().get(k).setAccess(1);
                    }
                }
            }
        }
    }

    private List<ModuleRightGUI> buildModuleRightDialog(long inputId, String strType) throws Exception {
        List<ModuleRightGUI> moduleRightGUIList;
        moduleRightGUIList = this.mmoduleRightGuiModel.getListModuleRight(inputId, strType, this.mstrAppCode);

        for(int iCount = 0; iCount < moduleRightGUIList.size(); ++iCount) {
            ((ModuleRightGUI)moduleRightGUIList.get(iCount)).setIndex((long)iCount);
            if (Authorizator.checkAuthorizator(((ModuleRightGUI)moduleRightGUIList.get(iCount)).getModulePath()).isEmpty()) {
                moduleRightGUIList.remove(iCount--);
            }
        }

        if (moduleRightGUIList.isEmpty()) {
            moduleRightGUIList.add(new ModuleRightGUI());
        }
        return moduleRightGUIList;
    }

    public void onRowExpand(UserGUI ett) throws Exception {
        this.mtmpUserGUI = ett;
        if (ett.getListGroup() == null || ett.getListGroup().isEmpty()) {
            UserGUI userMoreInfo = this.muserGuiModel.getUserGroupById(ett.getUser().getUserId());
            ett.setListGroup(userMoreInfo.getListGroup());
            ett.setBranch(userMoreInfo.getBranch());
            ett.setDistrict(userMoreInfo.getDistrict());
        }

    }

    public void changeStateAddUser() throws Exception {
        super.changeStateAdd();
        this.miAction = 1;
        this.mtmpUserGUI = new UserGUI();
        this.mtmpUserGUI.getListGroup().add(this.mtmpGroupGUI.getGroup());
        this.mstrUserNewPassword = "";
        this.mstrUserReNewPassword = "";
//        this.buildModuleRight("null");
        this.buildModuleRightUser("group");
        this.mselectedPickGroup = new ArrayList();
        this.mselectedPickGroup.add(new GroupGUI((GroupDTL)this.mtmpUserGUI.getListGroup().get(0)));
    }

    public void changeStateViewUser(UserGUI user) throws Exception {
        this.changeStateEditUser(user);
        super.changeStateView();
    }

    public void changeStateEditUser(UserGUI user) throws Exception {
        super.changeStateEdit();
        this.selectedIndex = this.mtmpGroupGUI.getListUser().indexOf(user);
        this.mtmpUserGUI = (UserGUI)SerializationUtils.clone(user);
        this.miAction = 1;
        this.mstrUserNewPassword = "";
        this.mstrUserReNewPassword = "";
        if (this.mtmpUserGUI.getListGroup() == null || this.mtmpUserGUI.getListGroup().isEmpty()) {
            UserGUI userMoreInfo = this.muserGuiModel.getUserGroupById(this.mtmpUserGUI.getUser().getUserId());
            this.mtmpUserGUI.setListGroup(userMoreInfo.getListGroup());
            this.mtmpUserGUI.setBranch(userMoreInfo.getBranch());
            this.mtmpUserGUI.setDistrict(userMoreInfo.getDistrict());
        }

        this.mstrUserOldStatus = this.mtmpUserGUI.getUser().getStatus();
        this.mselectedBranch = this.mtmpUserGUI.getBranch();
        this.onBranchChange();
        this.mselectedDistrict = this.mtmpUserGUI.getDistrict();
        this.buildModuleRight("user");
        List<GroupGUI> tmpListGroupGUI = new ArrayList();
        Iterator var3 = this.mtmpUserGUI.getListGroup().iterator();

        while(var3.hasNext()) {
            GroupDTL tmpGroupGUI = (GroupDTL)var3.next();
            tmpListGroupGUI.add(new GroupGUI(tmpGroupGUI));
        }

        this.mselectedPickGroup = tmpListGroupGUI;
    }

    public void changeStateCopyUser(UserGUI ett) throws Exception {
        super.changeStateCopy();
        this.miAction = 1;
        this.mstrUserNewPassword = "";
        this.mstrUserReNewPassword = "";
        this.mtmpUserGUI = (UserGUI)SerializationUtils.clone(ett);
        if (this.mtmpUserGUI.getListGroup() == null || this.mtmpUserGUI.getListGroup().isEmpty()) {
            UserGUI userMoreInfo = this.muserGuiModel.getUserGroupById(this.mtmpUserGUI.getUser().getUserId());
            this.mtmpUserGUI.setListGroup(userMoreInfo.getListGroup());
            this.mtmpUserGUI.setBranch(userMoreInfo.getBranch());
            this.mtmpUserGUI.setDistrict(userMoreInfo.getDistrict());
        }

        this.mstrUserOldStatus = this.mtmpUserGUI.getUser().getStatus();
        this.mselectedBranch = this.mtmpUserGUI.getBranch();
        this.onBranchChange();
        this.mselectedDistrict = this.mtmpUserGUI.getDistrict();
        this.buildModuleRight("user");
    }

    public void handDeleteUser(UserGUI user) throws Exception {
        if (this.isIsAllowDelete()) {
            if (user == null) {
                this.muserGuiModel.delete(this.mselectedUser);
            } else {
                this.muserGuiModel.delete(Collections.singletonList(user));
            }

            this.resetTreeGroup();
            this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
            this.mlistUser = this.muserGuiModel.getListAmUser();
            this.mselectedUser = null;
            ClientMessage.log(MESSAGE_TYPE.DELETE);
        }
    }

    private void handOkUser() throws Exception {
        if (this.mtmpUserGUI.getUser().getStatus() == 2L && !AdminUser.isSuperAdmin()) {
            throw new TelsoftException("can_not_set_system_user");
        } else {
            boolean isExistEmail;
            if (!this.isADD && !this.isCOPY) {
                if (this.isEDIT) {
                    if (!this.getPermission("U")) {
                        return;
                    }

                    if (!this.updateUserNewPassword()) {
                        return;
                    }

                    isExistEmail = this.muserGuiModel.isExistEmail(this.mtmpUserGUI, true);
                    if (isExistEmail) {
                        ClientMessage.logPErrUpdate("email_is_exist");
                        return;
                    }

                    this.updateUserLockedDate();
                    this.muserGuiModel.update(this.mtmpUserGUI, this.mlistModuleRight, this.mselectedBranch, this.mselectedDistrict);
                    this.resetTreeGroup();
                    this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
                    ClientMessage.logUpdate();
                }
            } else {
                if (!this.getPermission("I")) {
                    return;
                }

                if (!this.updateUserNewPassword()) {
                    return;
                }

                isExistEmail = this.muserGuiModel.isExistEmail(this.mtmpUserGUI, false);
                if (isExistEmail) {
                    ClientMessage.logPErrAdd("email_is_exist");
                    return;
                }

                this.updateUserLockedDate();
                if (this.mtmpUserGUI.getUser().getStatus() == 0L) {
                    this.mtmpUserGUI.getUser().setLockedDate(new Date());
                }

                this.muserGuiModel.add(this.mtmpUserGUI, this.mlistModuleRight, this.mselectedBranch, this.mselectedDistrict, this.isADD ? "add" : "copy");
                this.resetTreeGroup();
                this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
                this.mlistUser.add(0, this.mtmpUserGUI);
                ClientMessage.logAdd();
                if (this.isADD) {
                    this.mstrUserNewPassword = "";
                    this.mstrUserReNewPassword = "";
                    this.mtmpUserGUI = new UserGUI();
                    this.mtmpUserGUI.getListGroup().add(this.mtmpGroupGUI.getGroup());
                    this.mlistModuleRight = new ArrayList();
                    this.mlistModuleRight.add(new ModuleRightGUI());
                }
            }

        }
    }

    private boolean updateUserNewPassword() throws Exception {
        if (this.mstrUserNewPassword != null && !this.mstrUserNewPassword.isEmpty()) {
            if (!this.mstrUserNewPassword.equals(this.mstrUserReNewPassword)) {
                ClientMessage.logPErr("re_password_not_correct");
                return false;
            }

            if (PolicyProcessor.getPolicy("REQUIRE_STRONG_PASSWORD").equals("1")) {
                String strMinimumPasswordLength = PolicyProcessor.getPolicy("MINIMUM_PASSWORD_LENGTH");
                if (!strMinimumPasswordLength.isEmpty() && this.mstrUserNewPassword.length() < Integer.parseInt(strMinimumPasswordLength)) {
                    String strErrorMess = ResourceBundleUtil.getModuleObjectAsString("password_length_error");
                    ClientMessage.logPErr(strErrorMess.replace("{1}", strMinimumPasswordLength));
                    return false;
                }
            }

            this.mtmpUserGUI.getUser().setPassword(StringUtil.encryptPassword(this.mstrUserNewPassword));
            this.mtmpUserGUI.getUser().setModifiedPassword(new Date());
        } else {
            this.mtmpUserGUI.getUser().setModifiedPassword((Date)null);
        }

        return true;
    }

    private void updateUserLockedDate() {
        this.mtmpUserGUI.getUser().setLockedDate((Date)null);
        if (this.mstrUserOldStatus != this.mtmpUserGUI.getUser().getStatus() && this.mtmpUserGUI.getUser().getStatus() == 0L) {
            this.mtmpUserGUI.getUser().setLockedDate(new Date());
        }

        this.mstrUserOldStatus = this.mtmpUserGUI.getUser().getStatus();
    }

    public String getStrUserGroupList() {
        String strReturn = "";

        GroupDTL group;
        for(Iterator var2 = this.mtmpUserGUI.getListGroup().iterator(); var2.hasNext(); strReturn = strReturn + group.getName() + ", ") {
            group = (GroupDTL)var2.next();
        }

        strReturn = StringUtil.removeLastChar(strReturn);
        return strReturn;
    }

    public void setStrUserGroupList(String strInput) {
    }

    public List<String> getListUserGroup(List<GroupDTL> listGroup) {
        List<String> listReturn = new ArrayList();
        if (listGroup == null) {
            return listReturn;
        } else {
            Iterator var3 = listGroup.iterator();

            while(var3.hasNext()) {
                GroupDTL group = (GroupDTL)var3.next();
                listReturn.add(group.getName());
            }

            return listReturn;
        }
    }

    public String getUserStatus(int input) {
        return this.mstrStatus[input];
    }

    public String getUserExpireStatus(int input) {
        return this.mstrExpireStatus[input];
    }

    private void buildModuleRightUser(String strType) throws Exception {
        long inputId = 0L;
        inputId = this.mtmpGroupGUI.getGroup().getGroupId();

        this.mlistModuleRight = this.mmoduleRightGuiModel.getListModuleRight(inputId, strType, this.mstrAppCode);

        for(int iCount = 0; iCount < this.mlistModuleRight.size(); ++iCount) {
            ((ModuleRightGUI)this.mlistModuleRight.get(iCount)).setIndex((long)iCount);
            if (Authorizator.checkAuthorizator(((ModuleRightGUI)this.mlistModuleRight.get(iCount)).getModulePath()).isEmpty()) {
                this.mlistModuleRight.remove(iCount--);
            }
        }

        if (this.mlistModuleRight.isEmpty()) {
            this.mlistModuleRight.add(new ModuleRightGUI());
        }

    }

    private void buildModuleRight(String strType) throws Exception {
        long inputId = 0L;
        if (this.miAction == 1) {
            inputId = this.mtmpUserGUI.getUser().getUserId();
        } else if (this.miAction == 2) {
            inputId = this.mtmpGroupGUI.getGroup().getGroupId();
        }

        this.mlistModuleRight = this.mmoduleRightGuiModel.getListModuleRight(inputId, strType, this.mstrAppCode);

        for(int iCount = 0; iCount < this.mlistModuleRight.size(); ++iCount) {
            ((ModuleRightGUI)this.mlistModuleRight.get(iCount)).setIndex((long)iCount);
            if (Authorizator.checkAuthorizator(((ModuleRightGUI)this.mlistModuleRight.get(iCount)).getModulePath()).isEmpty()) {
                this.mlistModuleRight.remove(iCount--);
            }
        }

        if (this.mlistModuleRight.isEmpty()) {
            this.mlistModuleRight.add(new ModuleRightGUI());
        }

    }

    public void changeStateCopy() throws Exception {
    }

    public void handleCancel() throws Exception {
        if (this.miAction == 2 && (this.isADD || this.isCOPY || this.isEDIT)) {
            this.mtmpGroupGUI = (GroupGUI)this.mlistGroupGUI.get(this.selectedIndex);
            if (this.mrecentAddedGroup != null) {
                this.mtmpGroupGUI = this.mrecentAddedGroup;
            }

            this.sortListGroup();
            this.resetTreeGroup();
            this.mtmpGroupGUI.setListUser(this.getListUserByGroup(true));
        }

        super.handleCancel();
        long groupId = this.mtmpGroupGUI.getGroup().getGroupId();
        PrimeFaces.current().executeScript("scrollTreeTo('group_" + groupId + "')");
    }

    private void sortListGroup() {
        GroupGUI g0 = (GroupGUI)this.mlistGroupGUI.get(0);
        GroupGUI g1 = (GroupGUI)this.mlistGroupGUI.get(1);
        this.mlistGroupGUI.remove(0);
        this.mlistGroupGUI.remove(0);
        this.mlistGroupGUI.sort(new Comparator<GroupGUI>() {
            public int compare(GroupGUI groupGUI, GroupGUI t1) {
                return groupGUI.getGroup().getName().compareTo(t1.getGroup().getName());
            }
        });
        this.mlistGroupGUI.add(0, g1);
        this.mlistGroupGUI.add(0, g0);
    }

    public void handleDelete() throws Exception {
    }

    public void handleOK() throws Exception {
        if (this.miAction == 1) {
            this.handOkUser();
        } else if (this.miAction == 2) {
            this.handOkGroup();
        }

    }

    public void onAppChange() throws Exception {
        if (this.miAction == 1) {
            if (this.isADD) {
                this.buildModuleRight("null");
            } else {
                this.buildModuleRight("user");
            }
        } else if (this.miAction == 2) {
            if (this.isADD) {
                this.buildModuleRight("null");
            } else {
                this.buildModuleRight("group");
            }
        }

    }

    public void onBranchChange() throws Exception {
        this.mlistDistrict = this.mpayAreaGuiModel.getListDistrictByBranch(this.mselectedBranch);
    }

    public String removeSign(String strInput) {
        return StringUtil.removeSign(strInput);
    }

    public boolean isIsRootTree() {
        return this.mtmpGroupGUI.getGroup().getLevel() <= 1;
    }

    public List<GroupGUI> getMselectedPickGroup() {
        return this.mselectedPickGroup;
    }

    public void setMselectedPickGroup(List<GroupGUI> mselectedPickGroup) {
        this.mselectedPickGroup = mselectedPickGroup;
    }

    public GroupGUI getMselectedPickParentGroupGUI() {
        return this.mselectedPickParentGroupGUI;
    }

    public void setMselectedPickParentGroupGUI(GroupGUI mselectedPickParentGroupGUI) {
        this.mselectedPickParentGroupGUI = mselectedPickParentGroupGUI;
    }

    public boolean isMbIsIncludeChild() {
        return this.mbIsIncludeChild;
    }

    public void setMbIsIncludeChild(boolean mbIsIncludeChild) {
        this.mbIsIncludeChild = mbIsIncludeChild;
    }

    public TreeNode getMtreeGroup() {
        return this.mtreeGroup;
    }

    public TreeNode getMselectedGroupNode() {
        return this.mselectedGroupNode;
    }

    public void setMselectedGroupNode(TreeNode tn) {
        this.mselectedGroupNode = tn;
    }

    public GroupGUI getMtmpGroupGUI() {
        return this.mtmpGroupGUI;
    }

    public DualListModel<UserGUI> getMdualListUser() {
        return this.mdualListUser;
    }

    public void setMdualListUser(DualListModel<UserGUI> mdualListUser) {
        this.mdualListUser = mdualListUser;
    }

    public List<ModuleRightGUI> getMlistModuleRight() {
        return this.mlistModuleRight;
    }

    public void setMlistModuleRight(List<ModuleRightGUI> mlistModuleRight) {
        this.mlistModuleRight = mlistModuleRight;
    }

    public List<GroupGUI> getMlistGroupGUI() {
        return this.mlistGroupGUI;
    }

    public UserGUI getMtmpUserGUI() {
        return this.mtmpUserGUI;
    }

    public void setMtmpUserGUI(UserGUI mtmpUserGUI) {
        this.mtmpUserGUI = mtmpUserGUI;
    }

    public List<UserGUI> getMselectedUser() {
        return this.mselectedUser;
    }

    public void setMselectedUser(List<UserGUI> mselectedUser) {
        this.mselectedUser = mselectedUser;
    }

    public List<UserGUI> getMlistUser() {
        return this.mlistUser;
    }

    public int getAction() {
        return this.miAction;
    }

    public String getMstrUserNewPassword() {
        return this.mstrUserNewPassword;
    }

    public void setMstrUserNewPassword(String mstrUserNewPassword) {
        this.mstrUserNewPassword = mstrUserNewPassword;
    }

    public String getMstrUserReNewPassword() {
        return this.mstrUserReNewPassword;
    }

    public void setMstrUserReNewPassword(String mstrUserReNewPassword) {
        this.mstrUserReNewPassword = mstrUserReNewPassword;
    }

    public String getStrCheckedGroups() {
        return this.strCheckedGroups;
    }

    public void setStrCheckedGroups(String strCheckedGroups) {
        this.strCheckedGroups = strCheckedGroups;
    }

    public List<PayAreaGUI> getMlistDistrict() {
        return this.mlistDistrict;
    }

    public List<PayAreaGUI> getMlistBranch() {
        return this.mlistBranch;
    }

    public String getMselectedBranch() {
        return this.mselectedBranch;
    }

    public void setMselectedBranch(String mselectedBranch) {
        this.mselectedBranch = mselectedBranch;
    }

    public String getMselectedDistrict() {
        return this.mselectedDistrict;
    }

    public void setMselectedDistrict(String mselectedDistrict) {
        this.mselectedDistrict = mselectedDistrict;
    }

    public List<AppGUI> getMlistAppGUI() {
        return this.mlistAppGUI;
    }

    public String getMstrAppCode() {
        return this.mstrAppCode;
    }

    public void setMstrAppCode(String mstrAppCode) {
        this.mstrAppCode = mstrAppCode;
    }

    public List<UserGUI> getMselectedUserLazySelected() {
        return this.mselectedUserLazySelected;
    }

    public void setMselectedUserLazySelected(List<UserGUI> mselectedUserLazySelected) {
        this.mselectedUserLazySelected = mselectedUserLazySelected;
    }

    public List<UserGUI> getMselectedUserLazy() {
        return this.mselectedUserLazy;
    }

    public void setMselectedUserLazy(List<UserGUI> mselectedUserLazy) {
        this.mselectedUserLazy = mselectedUserLazy;
    }

    public List<UserGUI> getMlistUserGroupSelected() {
        return this.mlistUserGroupSelected;
    }

    public List<UserGUI> getMlistUserGroup() {
        return this.mlistUserGroup;
    }

    public String getMstrGroupSearch() {
        return this.mstrGroupSearch;
    }

    public void setMstrGroupSearch(String mstrGroupSearch) {
        this.mstrGroupSearch = mstrGroupSearch;
    }
}
