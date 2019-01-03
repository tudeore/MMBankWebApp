package com.mmbank.webapp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.SavingsAccountServiceImpl;
import com.moneymoney.exception.AccountNotFoundException;

//@WebServlet("/app/*")
@WebServlet("*.mm")
public class AccountController extends HttpServlet {

	private SavingsAccountServiceImpl savingsAccountService;
	private RequestDispatcher dispatcher;

	@Override
	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection
					("jdbc:mysql://localhost:3306/bankapp_db", "root", "root");
			PreparedStatement preparedStatement = 
					connection.prepareStatement("DELETE FROM ACCOUNT");
			preparedStatement.execute();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		savingsAccountService = new SavingsAccountServiceImpl();
	}
	
	boolean toSortIn=false;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getServletPath();
		switch (path) {
		case "/addNewSA.mm":
			response.sendRedirect("addNewSAForm.jsp");
			break;
		case "/addNew.mm":
			String accountHolderName = request.getParameter("txtAccHN");
			double accountBalance = Double.parseDouble(request.getParameter("txtBalance"));
			boolean salary = request.getParameter("rdSalary").equalsIgnoreCase("yes")?true:false;

			try {
				savingsAccountService.createNewAccount(accountHolderName, accountBalance, salary);
				response.sendRedirect("getAll.mm");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			
			break;
		case "/searchForm.mm":
			response.sendRedirect("SearchForm.jsp");
			break;
		case "/search.mm":
			int accountNumber = Integer.parseInt(request.getParameter("txtAccountNumber"));
			try {
				SavingsAccount account = savingsAccountService.getAccountById(accountNumber);
				request.setAttribute("account", account);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException | AccountNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case "/getAll.mm":
			try {
				List<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
				request.setAttribute("accounts", accounts);
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "/sortByName.mm":
			try {
				//Collection<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccount();
				ArrayList<SavingsAccount> accounts=(ArrayList<SavingsAccount>)savingsAccountService.getAllSavingsAccount();
				toSortIn = !toSortIn;
				int sort = toSortIn == false?1:-1;
				Collections.sort(accounts,new Comparator<SavingsAccount>(){
					@Override
					public int compare(SavingsAccount accountOne, SavingsAccount accountTwo) {
						return accountOne.getBankAccount().getAccountHolderName().compareToIgnoreCase
								(accountTwo.getBankAccount().getAccountHolderName());
					}
				});
				request.setAttribute("accounts", accounts);
				/*accountSet.addAll(accounts);
				request.setAttribute("accounts", accountSet);*/
				dispatcher = request.getRequestDispatcher("AccountDetails.jsp");
				dispatcher.forward(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		
		case "/sortForm.mm":
			
			break;
		default:
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
