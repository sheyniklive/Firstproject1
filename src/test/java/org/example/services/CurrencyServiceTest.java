package org.example.services;


import org.example.cbr.Proxy;
import org.example.service.CurrencyService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private Proxy proxy;

    @InjectMocks
    private CurrencyService currencyService;








}
