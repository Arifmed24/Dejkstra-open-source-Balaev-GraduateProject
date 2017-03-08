<%@include file="./../header.jsp" %>
<div class="main_body">
    <section>
        <div class="body_header">
            <h1> Find the way</h1>
        </div>
    </section>
    <div class="rail_form">
        <form action="/findway" method="POST">
            <div>
                <font color="red"><b><c:out value="${errorStation}"/></b></font>
                <label> Station departure:</label>
                <select name="stationDep">
                    <c:forEach items="${stations}" var="s">
                        <option value="${s.idStation}">${s.stationName}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label> Station arrival:</label>
                <select name="stationArr">
                    <c:forEach items="${stations}" var="s">
                        <option value="${s.idStation}">${s.stationName}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <div class="form-group">
                    <label>Date departure: </label>
                    <div class="input-group date" id="datetimepicker1">
                        <c:if test="${empty errorD}">
                            <input data-format="MM-dd-yyyy HH:mm:ss" type="text" name="dateDep"/>
                            <span class="input-group-addon">
                                     <span class="glyphicon-calendar glyphicon"></span>
                                 </span>
                        </c:if>
                        <c:if test="${!empty errorD}">
                            <input data-format="MM-dd-yyyy HH:mm:ss" type="text" name="dateDep"/>
                            <span class="input-group-addon">
                                    <span class="glyphicon-calendar glyphicon"></span>
                                 </span>
                            <font color="red"><b><c:out value="${errorD}"/></b></font>
                        </c:if>
                    </div>
                    <label>Date arrival: </label>
                    <div class="input-group date" id="datetimepicker2">
                        <c:if test="${empty errorA}">
                            <input name="dateArr" id="date" type="text">
                            <span class="input-group-addon">
                                    <span class="glyphicon-calendar glyphicon"></span>
                             </span>
                        </c:if>
                        <c:if test="${!empty errorA}">
                            <input name="dateArr" id="date" type="text">
                            <span class="input-group-addon">
                                    <span class="glyphicon-calendar glyphicon"></span>
                                 </span>
                            <font color="red"><b><c:out value="${errorA}"/></b></font>
                        </c:if>
                    </div>
                </div>
                <script type="text/javascript">
                    var start = "<?=$date?>";
                    var date = new Date();
                    var today = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                    $(function () {
                        $('#datetimepicker1').datetimepicker({
                            language: 'ru',
                            useSeconds: 'true',
                            format: 'DD/MM/YYYY',
                            defaultDate: start,
                            pickTime: false,
                            minDate: today
                        });
                    });
                </script>
                <script type="text/javascript">
                    var start = "<?=$date?>";
                    var date = new Date();
                    var today = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                    $(function () {
                        $('#datetimepicker2').datetimepicker({
                            language: 'ru',
                            useSeconds: 'true',
                            format: 'DD/MM/YYYY',
                            defaultDate: start,
                            pickTime: false,
                            minDate: today
                        });
                    });
                </script>
            </div>
            <div class="double-button">
                <button name="search" type="submit" value="ways">Find way</button>
                <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                    <button name="search" type="submit" value="passengers">Find passengers</button>
                </c:if>
            </div>
        </form>
    </div>
</div>
<%@include file="./../bottom.jsp" %>
