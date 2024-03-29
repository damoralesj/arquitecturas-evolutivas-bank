package co.edu.eafit.bank.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.edu.eafit.bank.domain.Customer;
import co.edu.eafit.bank.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	Validator validator;
	

	@Override
	@Transactional(readOnly = true)
	public List<Customer> findAll() {
		// TODO Auto-generated method stub
		return customerRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Customer> findById(Integer id) {
		// TODO Auto-generated method stub
		return customerRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Customer save(Customer entity) throws Exception {
		// TODO Auto-generated method stub
		
		//Validar que la entidad recibida sea un objeto
		if (entity == null) {
			throw new Exception("El customer es nulo");
		}
		
		//Validaciones definidas en el modelo domain
		validate(entity);
		
		//Validacion que el customer no exista previamente
		if(customerRepository.existsById(entity.getCustId()) == true) {
			throw new Exception("El customer ya existe");
		}
		
		return customerRepository.save(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Customer update(Customer entity) throws Exception {
		// TODO Auto-generated method stub
		
		//Validar que la entidad recibida sea un objeto
		if (entity == null) {
			throw new Exception("El customer es nulo");
		}
		
		//Validaciones definidas en el modelo domain
		validate(entity);
		
		//Validacion que el customer no exista previamente
		if(customerRepository.existsById(entity.getCustId()) == false) {
			throw new Exception("El customer no existe");
		}
		
		return customerRepository.save(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Customer entity) throws Exception {
		// TODO Auto-generated method stub

		//Validar que la entidad recibida sea un objeto
		if (entity == null) {
			throw new Exception("El customer es nulo");
		}
		
		//Validaciones definidas en el modelo domain
		validate(entity);
		
		//Validacion que el customer no exista previamente
		if(customerRepository.existsById(entity.getCustId()) == false) {
			throw new Exception("El customer no existe");
		}
		
		findById(entity.getCustId()).ifPresent(customer-> {
			
			if (customer.getAccounts()!= null && customer.getAccounts().isEmpty() == false) {
				throw new RuntimeException ("El customer tiene cuentas asociadas");
			}
			
			if (customer.getRegisteredAccounts()!=null && customer.getRegisteredAccounts().isEmpty() == false) {
				throw new RuntimeException("El customer tiene cuentas registradas");
			}
			
			customerRepository.deleteById(entity.getCustId());

		});
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		
		if (id ==null) {
			throw new Exception("El id es nulo");
		}
		
		if (customerRepository.existsById(id)) {
			delete(customerRepository.findById(id).get());
		}

	}

	@Override
	public void validate(Customer entity) throws Exception {
		// TODO Auto-generated method stub
		Set<ConstraintViolation<Customer>> constrainsViolations = validator.validate(entity);
		
		if (constrainsViolations.isEmpty()==false) {
			throw new ConstraintViolationException(constrainsViolations);
		}

	}

	@Override
	public Long count() {
		// TODO Auto-generated method stub
		return customerRepository.count();
	}

}
