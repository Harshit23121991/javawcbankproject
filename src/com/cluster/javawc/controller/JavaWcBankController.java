package com.cluster.javawc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class JavaWcBankController extends HttpServlet {
	Connection con;
	
	// init() method to create connection
	public void init(ServletConfig config) throws ServletException 
	{
		String strDriver = config.getInitParameter("driver");
		String strUrl = config.getInitParameter("dburl");
		String strUserId = config.getInitParameter("dbuid");
		String strPassword = config.getInitParameter("dbpwd");
		try 
		{
		    Class.forName(strDriver);
			con = DriverManager.getConnection(strUrl, strUserId, strPassword);
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	// service method
	public void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		PrintWriter writer = response.getWriter();
		String strPath = request.getServletPath();
	//	System.out.println("Current servlet path is*************" + strPath);
		
		//date@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
		Date date = new Date();
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		session.setAttribute("LDATE", (dateFormat.format(date)));
		
		//session management*********************

		 if(!(strPath.equalsIgnoreCase("/signinpage.do")
				|| strPath.equalsIgnoreCase("/signuppage.do")
				|| strPath.equalsIgnoreCase("/createuser.do")
				|| strPath.equalsIgnoreCase("/mainpage.do")
				|| strPath.equalsIgnoreCase("/loginuser.do")))
				{
					if((session.getAttribute("ACCOUNT_NO") == null) && (session.getAttribute("USERNAME1") ==null))
					{
						writer.println("<html><body background='./images/reactos-web.jpg'><center>");
						writer.println("");
						writer.println("<hr></hr>");
						writer.println("<hr></hr>");
						writer.println("<h2><u><i><font color='orange'>CUSTOMER LOGIN PAGE</font></i></u></h2>");
						writer.println("<form action='./loginuser.do' method='post'> ");
						writer.println("<table>");
						writer.println("<tr><td><font color='orange'>User Name:</font></td><td><input type='text' name='uname'></td></tr>");
						writer.println("<tr><td><font color='orange'>Password:</font></td><td><input type='password' name='password'></td></tr>");
						writer.println("<tr><td><input type='submit' value='Login'></td></tr>");
						writer.println("</table>");
						writer.println("</form></body></html>");
						return ;
					   	}		

				}
		  // login page
		  if (strPath.equalsIgnoreCase("/signinpage.do")) {
			writer.println("<html>");
			writer.println("<body background='./images/reactos-web.jpg'>");
			writer.println("<h1>Please enter your login details</h1>");
			writer.println("<form action='./html/welcome.html' method='get'>");
			writer.println("<input type='submit' value='Home' />");
			writer.println("</form>");
			writer.println("<form action='./loginuser.do' method='post'>");
			writer.println("<center><table>");
			writer.println("<tr>");
			writer.println("<td>Enter your User Name*</td>");
			writer.println("<td><input type='text' name='uname'></td>");
			writer.println("</tr>");
			writer.println("<tr>");
			writer.println("<td>Enter your Password*</td>");
			writer.println("<td><input type='password' name='password'></td>");
			writer.println("</tr>");
			writer.println("<tr>");
			writer.println("<td colspan='2' align='right'><input type='submit' value='Login'></td>");
			writer.println("</tr>");
			writer.println("</form>");
			writer.println("</center>");
			writer.println("</table>");
			writer.println("</body>");
			writer.println("</html>");
		}
			// check the user values
		else if (strPath.equalsIgnoreCase("/loginuser.do")) 
		{
			boolean b = false;
			Statement statement = null;
			ResultSet resultSet = null;
			String strUName = request.getParameter("uname");
			String strPassword = request.getParameter("password");
			try {
				statement = con.createStatement();
				resultSet = statement
						.executeQuery("SELECT USERNAME,PASSWORD FROM JAVAWCBANK");
				while (resultSet.next()) {
					if ((strUName.equals(resultSet
							.getString("USERNAME")))
							&& (strPassword.equalsIgnoreCase(resultSet
									.getString("PASSWORD")))) {
						b = true;
						break;
					}
				}
				// login user success output
				if (b) {
					
					writer.println("<html><br><br><br><br><br>");
					writer.println("<body background='./images/Wallpaper-HD (1).jpg'><center>");
					ResultSet resultSet2  = null;
					resultSet2 = statement
							.executeQuery("SELECT ACCOUNT_ID FROM JAVAWCBANK WHERE USERNAME = '"
									+ strUName + "' ");
					while(resultSet2.next()){
					session.setAttribute("USERNAME1", strUName);
					int accountNo = resultSet2.getInt(1);
					session.setAttribute("ACCOUNT_NO", accountNo);
					writer.println("Hello Mr " + strUName + " you are Welcome.  ");
					writer.println("You Logged in at " + session.getAttribute("LDATE"));
					writer.println("<form action='./mainpage.do' method='get'>");
					writer.println("<h2><b><u><font color='red'>For more options click here<input type='submit' value='Transactions Page' /><font></h2></u><b>");
					writer.println("</center><br/>");
					}

					// else condition for login user failure
				} else {
					writer.println("<html>");
					writer.println("<body background='./images/reactos-web.jpg'>");
					writer.println("Hello Mr " + strUName
							+ " please re enter your correct details.(username is case sesitive..!) ");
					writer.println("</form>");
					writer.println("<br />");
					writer.println("<form action='./signinpage.do' method='get'>");
					writer.println("To go login page<input type='submit' value='Click Here' />");
					writer.println("</form>");
					writer.println("</body>");
					writer.println("</html>");
				}
				}catch (SQLException sqle) 
					{
						sqle.printStackTrace();
					}
	
				}else if (strPath.equalsIgnoreCase("/mainpage.do")) {
				writer.println("<html>");
				writer.println("<body background='./images/reactos-web.jpg'>");
				writer.println("<center>");
				writer.println("You Logged in at " + dateFormat . format (date));
				writer.println("<h1>JavaWcBank Welcomes You</h1>");
				writer.println("User name :"+session.getAttribute("USERNAME1")+"<br>");
				writer.println("Your Acc No :"+session.getAttribute("ACCOUNT_NO"));
				writer.println("<table border='2'>");
				writer.println("<tr>");
				writer.println("<form action='./deposit.do' method='get'>");
				writer.println("<td colspan='1' align='center' >To Deposit Amount :<input type='submit' value='Deposit'/></td>");
				writer.println("</form>");
				writer.println("</form>");
				writer.println("'<form action='./withdraw.do' method='get'>");
				writer.println("<td colspan='1' align='center'>To withdraw your  amount : <input type='submit' value='Withdraw' /></td>");
				writer.println("</form>");
				writer.println("'<form action='./transfer.do' method='get'>");
				writer.println("<td colspan='1' align='center'>To Transfer your  amount : <input type='submit' value='Transfer' /></td>");
				writer.println("</form>");
				writer.println("<form action='./balance.do' method='get'>");
				writer.println("<td colspan='1' align='center'>Check your balance : <input type='submit' value='Balance' /></td>");
				writer.println("</form>");
				writer.println("<br>");
				writer.println("<form action='./logout.do' method='get'>");
				writer.println("<td colspan='1' align='center'>Please logout before closing: <input type='submit' value='Logout' /></td>");
				writer.println("</form>");
				writer.println("</tr>");
				writer.println("</center>");
				writer.println("</body>");
				writer.println("</html>");
		}
		// deposit.do page to do deposit operations
		else if (strPath.equalsIgnoreCase("/deposit.do"))
			{
			writer.println("<html>");
			writer.println("<body background='./images/reactos-web.jpg'>");
			writer.println("User name :"+session.getAttribute("USERNAME1")+"<br>");
			writer.println("User name :"+session.getAttribute("ACCOUNT_NO"));
			writer.println("<form action='./depositsuccess.do' method='get'>");
			writer.println("<h2>Please provide your details</h2>");
			writer.println("<center><table border='2'>");
			writer.println("<tr>");
			writer.println("<td>Enter amount to deposit</td>");
			writer.println("<td><input type='text' name='damount'></td>");
			writer.println("</tr>");
			writer.println("<tr>");
			writer.println("<td colspan='2' align='right'><input type='submit' value='Deposit'></td>");
			writer.println("</center></form></tr></table>");
			writer.println("<br><br><br><br><br><form action='./mainpage.do' method='get'>");
			writer.println("<input type='submit' value='Back' />");
			writer.println("</form>");
			writer.println("</body></html>");
			}
			//deposit success page ,show total balance
		else if (strPath.equalsIgnoreCase("/depositsuccess.do")) 
			{
			String strDamount= request.getParameter("damount");
			String strName = (String)session.getAttribute("USERNAME1");
			Object object = session.getAttribute("ACCOUNT_NO");
			int intAcc=(Integer)object;
			ResultSet resultSet2= null;
			Statement statement1 = null ;
			try {
				double dblDamount = Double.parseDouble(strDamount);
				System.out.println("deposit amount="+dblDamount);
				System.out.println("******************************");
				statement1 = con.createStatement();
				resultSet2 = statement1.executeQuery("SELECT AMOUNT FROM JAVAWCBANK WHERE ACCOUNT_ID ="+intAcc+"");
				if(resultSet2.next())
				{
				double amount = resultSet2.getInt("AMOUNT");
				double newAmount = amount+dblDamount;
				statement1.executeUpdate("UPDATE JAVAWCBANK SET AMOUNT="+newAmount+" WHERE ACCOUNT_ID="+intAcc+"");
				writer.println("<html>");
				writer.println("<body background='./images/reactos-web.jpg'>");
				writer.println("User name : "+session.getAttribute("USERNAME1")+"<br>");
				writer.println("Your Acc No :"+session.getAttribute("ACCOUNT_NO"));
				writer.println("<center>");
				writer.println("<h1>Thank you Mr '"+strName+"'<br> </h1>");
				writer.println("Your total balance is :"+newAmount+" ");
				writer.println("</center>");
				writer.println("<br><br><br><br><br><form action='./mainpage.do' method='get'>");
				writer.println("<input type='submit' value='Back' />");
				writer.println("</form>");
				writer.println("</body>");
				writer.println("</html>");
				}
			}
			catch (SQLException e) 
				{
				writer.println("PLASE CHECK THE QUERY ");
				}
			catch(NullPointerException ne)
				{	
			writer.println("PLASE ENTER VALID AMOUNT ");
				}
			catch(NumberFormatException nfe)
				{
				writer.println("PLASE ENTER VALID NUMBER (0-9)ONLY ");
				}
			}	
			 //withdraw page.............
			 // withdraw.do page to do draw operations
			else if (strPath.equalsIgnoreCase("/withdraw.do")) 
				{
					writer.println("<html>");
					writer.println("<body background='./images/reactos-web.jpg'>");
					writer.println("User name :"+session.getAttribute("USERNAME1")+"<br>");
					writer.println("User name :"+session.getAttribute("ACCOUNT_NO"));
					writer.println("<form action='./withdrawsuccess.do' method='put'>");
					writer.println("<h2>Please provide your details</h2>");
					writer.println("<center><table border='2' >");
					writer.println("<tr>");
					writer.println("<td>Enter amount to withdraw from your SB account</td>");
					writer.println("<td><input type='text' name='wamount'></td>");
					writer.println("</tr>");
					writer.println("<tr>");
					writer.println("<td colspan='2' align='right'><input type='submit' value='Withdraw'></td>");
					writer.println("</tr></center></form></table>");
					writer.println("<br><br><br><br><form action='./mainpage.do' method='get'>");
					writer.println("<input type='submit' value='Back' />");
					writer.println("</body></html>");
				}
				//withdraw success page ,show total balance
				else if (strPath.equalsIgnoreCase("/withdrawsuccess.do")) 
				{
					String strWamount= request.getParameter("wamount");
					String strName = (String)session.getAttribute("USERNAME1");
					Object object = session.getAttribute("ACCOUNT_NO");
					int intAcc=(Integer)object;
					ResultSet resultSet2= null;
					Statement statement1 = null ;
					double amount = 0 ;
					double newAmount ;
					try {
						double dblWAmount = Double.parseDouble(strWamount);
						System.out.println("withdraw amount="+dblWAmount);
					statement1 = con.createStatement();
					resultSet2 = statement1.executeQuery("SELECT AMOUNT FROM JAVAWCBANK WHERE ACCOUNT_ID ="+intAcc+"");
					if(resultSet2.next())
						{
							amount = resultSet2.getInt("AMOUNT");
						}
					else
						{
							throw new Exception();
						}
					if (dblWAmount <= amount)
						{
							newAmount = amount-dblWAmount;
						}
					else
						{
							throw new Exception();
						}
					statement1.executeUpdate("UPDATE JAVAWCBANK SET AMOUNT="+newAmount+" WHERE ACCOUNT_ID="+intAcc+"");
					writer.println("<html>");
					writer.println("<body background='./images/reactos-web.jpg'>");
					writer.println("User name : " + session.getAttribute ("USERNAME1") + "<br>");
					writer.println("Your Acc No : " + session.getAttribute ("ACCOUNT_NO"));
					writer.println("<center>");
					writer.println("<h1>Thank you Mr '"+strName+"'<br> </h1>");
					writer.println("Your available total balance is :"+newAmount+" ");
					writer.println("</center>");
					writer.println("<br><br><br><br><form action='./mainpage.do' method='get'>");
					writer.println("<input type='submit' value='Back' />");
					writer.println("</form>");
					writer.println("</body>");
					writer.println("</html>");
				}
				catch (SQLException e) 
					{
						writer.println("PLASE CHECK THE QUERY ");
					}
					catch(NullPointerException ne)
					{	
					writer.println("PLASE ENTER VALID AMOUNT ");
					}
					catch(NumberFormatException nfe)
					{
						writer.println("PLASE ENTER VALID NUMBER (0-9)ONLY ");
					} catch (Exception e) 
					{
						writer.println("Please enter the amount which is less than your total balance."+amount+"");
					}
	              }
					//Transfer.do page ,to transfer amount.
					 else if (strPath.equalsIgnoreCase("/transfer.do")) {
						writer.println("<html>");
						writer.println("<body background='./images/reactos-web.jpg'>");
						writer.println("User name :"+session.getAttribute("USERNAME1")+"<br>");
						writer.println("User name :"+session.getAttribute("ACCOUNT_NO"));
						writer.println("<form action='./transfersuccess.do' method='put'>");
						writer.println("<h2>Please provide your details</h2>");
						writer.println("<center><table border='2'>");
						writer.println("<tr>");
						writer.println("<td>Enter account no to transfer amount</td>");
						writer.println("<td><input type='text' name='tfraccount'></td>");
						writer.println("</tr>");
						writer.println("<tr>");
						writer.println("<td>Enter amount to transfer from your SB account</td>");
						writer.println("<td><input type='text' name='tframount'></td>");
						writer.println("</tr>");
						writer.println("<tr>");
						writer.println("<td colspan='2' align='right'><input type='submit' value='Transfer'></td>");
						writer.println("</tr></center></form></table>");
						writer.println("<form action = './mainpage.do'>");
						writer.println("<br><br><br><br><input type='submit' value = 'Back'/></form>");
						writer.println("</body></html>");
					 }
						//transfer success page ,show total balance in both accounts
					 else if (strPath.equalsIgnoreCase("/transfersuccess.do"))
						  {
						String strTfrAccount = request.getParameter("tfraccount");
						String strTfrAmount = request.getParameter("tframount");
						String strName = (String)session.getAttribute("USERNAME1");
						Object srcAccObj = session.getAttribute("ACCOUNT_NO");
						int intSrcAccount=(Integer)srcAccObj;
						int intTfrAccount = Integer.parseInt(strTfrAccount);
						ResultSet resultSet2 = null ;
						Statement statement1 = null ;
						double oldAmount = 0 ;
						double newAmount ;
						try {
							double dblTfrAmount = Double.parseDouble(strTfrAmount);
							System.out.println("Transfered amount is = "+dblTfrAmount);
						statement1 = con.createStatement();
						//transaction starts 
						con.setAutoCommit(false); 
						
						resultSet2 = statement1.executeQuery("SELECT AMOUNT FROM JAVAWCBANK WHERE ACCOUNT_ID ="+intSrcAccount+"");
							if(resultSet2.next())
							{
								 oldAmount = resultSet2.getInt("AMOUNT");
							}
							else
							{
								throw new Exception();
							}
							if (dblTfrAmount <= oldAmount)
							{
								 newAmount = oldAmount-dblTfrAmount;
							}
							else
							{
								writer.println("Please enter valid amount to transfer.");
								throw new Exception();
							
							}
						statement1.executeUpdate("UPDATE JAVAWCBANK SET AMOUNT="+newAmount+" WHERE ACCOUNT_ID="+intSrcAccount+"");
						//get amount from destination account..
						resultSet2 = statement1.executeQuery("SELECT AMOUNT FROM JAVAWCBANK WHERE ACCOUNT_ID ="+intTfrAccount+"");
						double desAmount = 0.00;
						double desNewAmount ;
						if (resultSet2.next())
							{
								desAmount=resultSet2.getInt("AMOUNT");
							}
						else
							{
								throw new Exception();
							}
								desNewAmount = desAmount + dblTfrAmount ;
					//update destination account balance
						statement1.executeUpdate("UPDATE JAVAWCBANK SET AMOUNT="+desNewAmount+" WHERE ACCOUNT_ID="+intTfrAccount+"");
						con.commit();
						writer.println("<html>");
						writer.println("<body background='./images/reactos-web.jpg'>");
						writer.println("User name :"+session.getAttribute("USERNAME1")+"<br>");
						writer.println("Your Acc No :"+session.getAttribute("ACCOUNT_NO"));
						writer.println("<center>");
						writer.println("<h1>Thank you Mr '"+strName+"'<br> </h1>");
						writer.println("You have transfered RS "+dblTfrAmount+" to account no : " + intTfrAccount + " successfully");
						writer.println("Your available total balance is :"+newAmount+" ");
						writer.println("</center>");
						writer.println("<form action='./mainpage.do' method='get'>");
						writer.println("<input type='submit' value='Back' />");
						writer.println("</form>");
						writer.println("</body>");
						writer.println("</html>");
						}
						catch (SQLException e) 
						{
							writer.println("PLASE CHECK THE QUERY ");
						}
						catch(NullPointerException ne)
						{	
							writer.println("PLASE ENTER VALID AMOUNT ");
						}
						catch(NumberFormatException nfe)
						{
							writer.println("PLASE ENTER VALID NUMBER (0-9)ONLY ");
						} catch (Exception e) 
						{
							try 
							{
								writer.println("<html>");
								writer.println("<body bgcolor='whaet'>");
								writer.println("<center>");
								writer.println("Money not transferred succesfully....");
								writer.println("<br><br><br><br><form action='./transfer.do' method='get'>");
								writer.println("<input type='submit' value='Back' /></form>");
								writer.println("</center>");
								writer.println("<body>");
								writer.println("</html>");
								con.rollback(); // end of transaction
							} 
							catch (SQLException sqle)
							{
								sqle.printStackTrace();
							} 
						}
					}
					// balance.do page to check balance
						else if (strPath.equalsIgnoreCase("/balance.do")) 
						{
							Statement statement3 = null ;
							ResultSet resultSet3 = null;
							try {
							String strName = (String)session.getAttribute("USERNAME1");
							Object object = session.getAttribute("ACCOUNT_NO");
							int intAcc=(Integer)object;
							statement3 = con.createStatement();
							resultSet3 = statement3.executeQuery("SELECT AMOUNT FROM JAVAWCBANK WHERE ACCOUNT_ID ="+intAcc+"");
							if(resultSet3.next()){
							double amount = resultSet3.getDouble("AMOUNT");
							writer.println("<html>");
							writer.println("<body background='./images/reactos-web.jpg'><table><center>");
							writer.println("User name :"+session.getAttribute("USERNAME1")+"<br>");
							writer.println("User name :"+session.getAttribute("ACCOUNT_NO"));
							writer.println("<center>");
							writer.println("<h2>Hello Mr " + strName + " your current balance is : "+amount+"</h2>");
							writer.println("<br><br><br><br><form action = './mainpage.do'>");
							writer.println("<input type='submit' value = 'Back'/></form>");
							writer.println("</center></table></body></html>");
							}
						}
							catch (SQLException sqle) 
							{
								sqle.printStackTrace();
							}
					} 
					    //logout.do page *****************************
					else if (strPath.equalsIgnoreCase("/logout.do")) {
						String strName = (String)session.getAttribute("USERNAME1");
						@SuppressWarnings("unused")
						Object object = session.getAttribute("ACCOUNT_NO");
						writer.println("<html>");
						writer.println("<body background='./images/reactos-web.jpg'>");
						writer.println("You Logged out at " + dateFormat . format (date));
						writer.println("<center>");
						writer.println("<h2>Hello Mr " + strName + " <font color='orange'>"+"your are successfully logged out....!!</h2></font></center></table>");
						writer.println("<br><br><br><br><br><br><br><br><form action = './html/welcome.html'>");
						writer.println("<input type='submit' value = 'Back to Home '/></form>");
						writer.println("</body></html>");
						session.invalidate();
					
						  	// new user creation page to receive details
				} else if (strPath.equalsIgnoreCase("/signuppage.do")) {
					writer.println("<html><center>");
					writer.println("<body background='./images/reactos-web.jpg'>");
					writer.println("<h1>JavaWcBank</h1>");
					writer.println("<h2>Create User</h2>");
					writer.println("<form action='./createuser.do' method='post'>");
					writer.println("<table>");
					writer.println("<tr>");
					writer.println("<td>Enter your Name*</td>");
					writer.println("<td><input type='text' name='name'></td>");
					writer.println("</tr>");
					writer.println("<tr>");
					writer.println("<td>Enter your User name*</td>");
					writer.println("<td><input type='text' name='username'></td>");
					writer.println("</tr>");
					writer.println("<tr>");
					writer.println("<td>Enter your Password*</td>");
					writer.println("<td><input type='password' name='password'></td>");
					writer.println("</tr>");
					writer.println("<tr>");
					writer.println("<td>Please confirm your password*</td>");
					writer.println("<td><input type='password' name='cnfpassword'></td>");
					writer.println("</tr>");
					writer.println("<tr>");
					writer.println("<td>Enter your Email id*</td>");
					writer.println("<td><input type='text' name='emailid'></td>");
					writer.println("</tr>");
					writer.println("<tr>");
					writer.println("<td>Enter your  amount*</td>");
					writer.println("<td><input type='text' name='amount'></td>");
					writer.println("</tr>");
					writer.println("<tr>");
					writer.println("<td>Enter your Address*</td>");
					writer.println("<td><input type='text' name='address'></td>");
					writer.println("</tr>");
					writer.println("<tr>");
					writer.println("</tr>");
					writer.println("<tr>");
					writer.println("<td colspan='2' align='center'> <input type='submit' value='Create Account'></td>");
					writer.println("</tr>");
					writer.println("</form>");
					writer.println("</table>");
					writer.println("</form>");
					writer.println("<form action = './html/welcome.html'H>");
					writer.println("<input type='submit' value = 'Back to Home '/></form>");
					writer.println("</center></body>");
					writer.println("</html>");
				}
				// get the user details to signup/create user page
				else if (strPath.equalsIgnoreCase("/createuser.do")) {
					String strName = request.getParameter("name");
					String strUsername = request.getParameter("username");
					String strPassword = request.getParameter("password");
					String strCnfpassword = request.getParameter("cnfpassword");
					String strEmailid = request.getParameter("emailid");
					String strAmount = request.getParameter("amount");
					String strAddress = request.getParameter("address");
					//String strUName = strName.toUpperCase();
					//System.out.println(strUName);
					int intAmount = 0;
					try {
						intAmount = Integer.parseInt(strAmount);
					} catch (NumberFormatException nfe) {
						writer.println("<html><body background='./images/reactos-web.jpg'><center>");
						writer.println("<h1>JavaWC Bank</h1></center>");
						writer.println("<h2>Create User </h2><br/>");
						writer.println("Please enter valid amount");
						writer.println("<form action = './signuppage.do'>");
						writer.println("<input type='submit' value = 'Back'/><br>");
						writer.println("</form>");
						writer.println("</body></html>");
						return;
					}
					if (!strCnfpassword.equals(strPassword)) {
						writer.println("<html><body background='./images/reactos-web.jpg'><center>");
						writer.println("<h1>JavaWC Bank</h1></center>");
						writer.println("<h2>Create User </h2>");
						writer.println("Password not matched .<br/> Please enter correct password");
							writer.println("<form action = './signuppage.do'>");
						writer.println("<input type='submit' value = 'Back'/><br>");
						writer.println("</form>");
						writer.println("</body></html>");
						return;
					}
					// insert values into database and get values from database
					Statement statement = null;
					try {
						statement = con.createStatement();
						String insertQuery = "INSERT INTO JAVAWCBANK (ACCOUNT_ID,NAME,USERNAME,PASSWORD,EMAIL_ID,AMOUNT,ADDRESS)VALUES(ACCOUNT_ID_SEQ.NEXTVAL,'"
								+ strName
								+ "','"
								+ strUsername
								+ "','"
								+ strPassword
								+ "','"
								+ strEmailid
								+ "','"
								+ intAmount
								+ "','"
								+ strAddress + "')";
						statement.execute(insertQuery);
						ResultSet resultSet = statement
								.executeQuery("SELECT ACCOUNT_ID FROM JAVAWCBANK WHERE USERNAME = '"
										+ strUsername + "' ");
						resultSet.next();
						int accountNo = resultSet.getInt(1);
						System.out.println(strUsername);
						System.out.println(accountNo);
						writer.println("<body background='./images/reactos-web.jpg'><center>");
						writer.println("<h1>JavaWC Bank</h1><hr>");
						writer.println("<h2> User Created </h2>");
						writer.println("Hello Mr/Mrs " + strName + " <br>");
						writer.println("Your account is successfully created, And your account num is : <h1>"
								+ accountNo + "</h1><hr>");
						writer.println("<form action = './html/welcome.html'>");
						writer.println("<input type='submit' value = 'Home'/><br>");
						writer.println("</form>");
						writer.println("</center></body></html>");
		
				} catch (SQLException e) {
					writer.println("<html><body bgcolor='red'>PLease enter all details.");
					writer.println("<h3>PLease enter all details.<h3>");
					writer.println("<form action = './signuppage.do'>");
					writer.println("<input type='submit' value = 'Back'/><br>");
					writer.println("</form>");
					writer.println("</body></html>");
				}
		
			}
	     }
	}