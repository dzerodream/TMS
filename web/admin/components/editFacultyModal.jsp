<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <div id="editFacultyModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h2>教职工详情</h2>
                    <span class="close" onclick="closeEditModal()">&times;</span>
                </div>
                <div class="modal-body">
                    <form id="editFacultyForm">
                        <input type="hidden" id="editFacultyId" name="facultyId">
                        <div class="form-group">
                            <label for="editFacultyNumber">工号</label>
                            <input type="text" id="editFacultyNumber" disabled>
                        </div>
                        <div class="form-group">
                            <label for="editFacultyName">姓名</label>
                            <input type="text" id="editFacultyName" name="facultyName" required>
                        </div>
                        <div class="form-group">
                            <label for="editPassword">密码</label>
                            <input type="password" id="editPassword" name="password">
                            <div class="password-hint">留空则保持原密码不变</div>
                        </div>
                        <div class="form-group">
                            <label for="editPhoneNumber">联系手机号</label>
                            <input type="tel" id="editPhoneNumber" name="phoneNumber" required>
                        </div>
                        <div class="form-group">
                            <label for="editDepartmentId">所属院系</label>
                            <select id="editDepartmentId" name="departmentId" required>
                                <option value="">请选择院系</option>
                                <c:forEach items="${departmentList}" var="dept">
                                    <option value="${dept.deptId}">${dept.deptName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="editIdNumber">身份证号</label>
                            <input type="text" id="editIdNumber" name="idNumber" required>
                        </div>
                        <div class="form-group">
                            <label for="editRoleId">角色权限</label>
                            <select id="editRoleId" name="roleId" required>
                                <option value="">请选择角色</option>
                                <option value="1">超级管理员</option>
                                <option value="2">院系管理员</option>
                                <option value="3">普通教职工</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="closeEditModal()">取消</button>
                    <button type="button" class="btn-submit" onclick="submitEditFaculty()">保存</button>
                </div>
            </div>
        </div>