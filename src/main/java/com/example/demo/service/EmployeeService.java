package com.example.demo.service;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.LoginDto;
import com.example.demo.entity.Employee;
import com.example.demo.repo.EmployeeRepo;
import com.example.demo.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addEmployee(EmployeeDto employeeDto){
        Employee employee = new Employee(
                employeeDto.getEmpId(),
                employeeDto.getEmpName(),
                employeeDto.getEmail(),
                this.passwordEncoder.encode(employeeDto.getPassword())
        );
        employeeRepo.save(employee);
        return employee.getEmpName();
    }

    public LoginResponse loginEmployee(LoginDto loginDto) {
        String msg="";
        Employee employee1 = employeeRepo.findByEmail(loginDto.getEmail());

        if(employee1 != null){
            String password = loginDto.getPassword();
            String encodePassword = employee1.getPassword();
            boolean isPwdRight = passwordEncoder.matches(password,encodePassword);
            if(isPwdRight){
                Optional<Employee> employee = employeeRepo.findOneByEmailAndPassword(loginDto.getEmail(),loginDto.getPassword());
                if(employee.isPresent()){
                    return new LoginResponse("login success",true);
                }else{
                    return new LoginResponse("login failed",false);
                }
            }else {
                return new LoginResponse("password not matched",false);
            }
        }else {
            return new LoginResponse("email not exits",false);
        }

    }
}
