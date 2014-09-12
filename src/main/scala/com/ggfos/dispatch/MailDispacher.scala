package com.ggfos.dispatch

import java.util.Date
import java.util.HashMap

import org.easyutils.mail.MailUtility
import com.ggfos.common.ConfigProperties

object MailDispacher extends ConfigProperties {
  def send(errAddress: String, msg: String) = {
    val from: HashMap[String, String] = new HashMap()
    val content = s"""
 <div>
	<br>
</div>
<div>
	<includetail>
	<div>
		<br>
	</div>
	<style type="text/css">
body {
	background: #ffffff;
	margin: 0 auto;
	padding: 0;
	text-align: left;
	font-size: 12px;
	font-family: "微软雅黑", "宋体";
}

table {
	font-size: 12px;
	font-family: "微软雅黑", "宋体";
}

.table_c {
	border: 1px solid #ccc;
	border-collapse: collapse;
}

.table_c td {
	border: 1px solid #ccc;
	border-collapse: collapse;
}

.table_b {
	border: 1px solid #666;
	border-collapse: collapse;
}

.table_b td {
	border-collapse: collapse;
	border: 1px solid #ccc;
}

.table_b th {
	color: #fff;
	background: #666;
}

a:link {
	color: #3366cc;
	font-weight: normal;
}

a:visited {
	color: #3366cc;
}

a:hover {
	color: #000;
}

a:active {
	color: #3366cc;
}

td {
	line-height: 20px;
}
</style>
	<table width="850" border="0" cellspacing="0" align="center"
		cellpadding="0" style="border: #ccc 1px solid;">
		<tbody>
			<tr>
				<td>
					<table width="850" border="0" cellpadding="0" cellspacing="0"
						bgcolor="fbfbfb"
						style="border-bottom: #eeeeee 1px solid; border-top: #ffffff 1px solid;">
						<tbody>
							<tr>
								<td width="137"><img
									src="http://www.baidu.com/img/bdlogo.gif" width="188"
									height="52"></td>
								<td width="249" align="left"
									style="font-size: 12px; color: #999999; padding-top: 26px;">
									此信为系统邮件，请不要直接回复。</td>
								<td width="264" align="left"
									style="font-size: 12px; font-weight: bold; color: #999999; padding-top: 26px;">
									<a href="" target="_blank"><strong>
											<font color="#666666"> 首页 </font>
									</strong></a>&nbsp; <a href="" target="_blank"><strong>
											<font color="#666666"> 接口首页 </font>
									</strong></a>&nbsp; <a
									href="#"
									target="_blank"><strong> <font color="#666666">
												测试接口 </font>
									</strong></a>&nbsp; <a
									href="#"
									target="_blank"><strong> <font color="#666666">
												获取好友 </font>
									</strong></a>
								</td>
							</tr>
						</tbody>
					</table>
					<table width="592" border="0" align="center" cellspacing="0"
						cellpadding="0" style="margin: 28px 28px 10px 28px;">
						<tbody>
							<tr>
								<td
									style="font-size: 12px; color: #666666; padding-bottom: 6px;">
									Hi Gays</td>
							</tr>
						</tbody>
					</table>
					<table width="592" border="0" cellspacing="0" cellpadding="0"
						align="center">
						<tbody>
							<tr>
								<td
									style="font-size: 14px; color: 333333; font-weight: bold; padding-bottom: 15px;">
									位于<span style="color: #cc0000;">$errAddress</span>的mongo服务于${
      new Date()
    }<span style="color: #cc0000;">运行异常</span>，请及时核实并找相关人员处理，详细信息如下：
								</td>
							</tr>
							<tr>
								<td>
									<table width="592" border="0" align="center" class="table_c"
										style="border-collapse: collapse;">
										<tbody>
											<tr>
												<td width="296" height="28" bgcolor="#FFFFFF" align="center"
													style="border: 1px solid #ccc;">分片位置</td>
												<td width="296" height="28" bgcolor="#FFFFFF" align="center"
													style="border: 1px solid #ccc;">错误信息</td>
											</tr>
											<tr>
												<td width="296" height="28" bgcolor="#FFFFFF"
													style="border: 1px solid #ccc;" align="center">
													$errAddress</td>
												<td width="296" height="28" bgcolor="#FFFFFF" align="center"
													style="border: 1px solid #ccc;">$msg</td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
								<td
									style="font-size: 12px; line-height: 20px; color: 333333; padding-top: 15px;">
									<strong>重要提示：</strong> <br> 技术人员请参照文档： <br>*.doc
									<br> 非技术人员请联系后台小组： <br> 7*24小时服务热线：110
								</td>
							</tr>
						</tbody>
					</table>
					<table width="592" border="0" align="center" cellspacing="0"
						cellpadding="0"
						style="margin: 34px 28px 16px 28px; border-top: #e4e4e4 1px solid;">
						<tbody>
							<tr>
								<td
									style="font-size: 12px; line-height: 20px; color: #999999; padding-top: 6px;">
									如果遇到紧急情况，请联系如下工作人员： <br> <br>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
	</includetail>
</div>
"""
    from.put("primos", username);
    MailUtility.send(host, username, password, recipients, debug, content, mimeType, "it's sorry to send email to you", from);
  }
}