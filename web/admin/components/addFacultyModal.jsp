<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <div id="addFacultyModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h2>添加成员</h2>
                    <span class="close" onclick="closeAddModal()">&times;</span>
                </div>
                <div class="modal-body">
                    <form id="addFacultyForm">
                        <div class="form-group">
                            <label for="facultyName">姓名</label>
                            <input type="text" id="facultyName" name="facultyName" required>
                        </div>
                        <div class="form-group">
                            <label for="facultyId">工号</label>
                            <input type="text" id="facultyId" name="facultyId" required>
                        </div>
                        <div class="form-group">
                            <label for="phoneNumber">联系手机号</label>
                            <input type="tel" id="phoneNumber" name="phoneNumber" required>
                        </div>
                        <div class="form-group">
                            <label for="departmentId">所属院系</label>
                            <select id="departmentId" name="departmentId" required>
                                <option value="">请选择院系</option>
                                <c:forEach items="${departmentList}" var="dept">
                                    <option value="${dept.deptId}">${dept.deptName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="roleId">角色权限</label>
                            <select id="roleId" name="roleId" required>
                                <option value="">请选择角色</option>
                                <option value="1">超级管理员</option>
                                <option value="2">院系管理员</option>
                                <option value="3">普通教职工</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="password">初始密码</label>
                            <input type="password" id="password" name="password" required>
                        </div>
                        <div class="form-group">
                            <label for="idNumber">身份证号</label>
                            <input type="text" id="idNumber" name="idNumber" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="closeAddModal()">取消</button>
                    <button type="button" class="btn-submit" onclick="submitAddFaculty()">确定</button>
                </div>
            </div>
        </div>