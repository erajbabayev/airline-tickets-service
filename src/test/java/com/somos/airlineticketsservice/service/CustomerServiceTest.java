package com.somos.airlineticketsservice.service;

import com.somos.airlineticketsservice.entity.Customer;
import com.somos.airlineticketsservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a sample customer
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
    }

    @Test
    void testFindAll() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        // Act
        List<Customer> result = customerService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        Optional<Customer> result = customerService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Customer> result = customerService.findById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        // Arrange
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer result = customerService.save(customer);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testDeleteById() {
        // Act
        customerService.deleteById(1L);

        // Assert
        verify(customerRepository, times(1)).deleteById(1L);
    }
}