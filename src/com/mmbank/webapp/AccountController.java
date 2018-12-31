package com.mmbank.webapp;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("*.mm")
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 SavingsAccountService savingsAccountService=new SavingsAccountServiceImpl();
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getServletPath();
		switch(path){
		case "/closeAccount.mm" :
			response.sendRedirect("closeAccountForm.html");
			break;
		case "/deleteForm.mm" :
		{
			System.out.println(request.getServletPath());
			SavingsAccountService savingsAccountService = new SavingsAccountServiceImpl();
			System.out.println(request.getServletPath());
			
			int accountNumber = Integer.parseInt(request.getParameter("accountNumber"));
			System.out.println(accountNumber);
			try {
				System.out.println("hello");
				SavingsAccount deleteAccount = savingsAccountService.deleteAccount(accountNumber);
				System.out.println(deleteAccount);
				
			} 
			catch (ClassNotFoundException e) {
				System.out.println("byy");
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("htt");
				e.printStackTrace();
			}
		}
			break;
		case "/addAccount.mm" :
			response.sendRedirect("addAccountForm.html");
			break;
		case "/addAccountForm.mm":	
			String name = request.getParameter("Account Holder Name");
			double salary = Double.parseDouble(request.getParameter("Account Balance"));
			System.out.println("Account Created");
			try {
				savingsAccountService.createNewAccount(name, salary, true);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/updateAccount.mm" :
			response.sendRedirect("updateAccountForm.html");
		}
			
		
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
