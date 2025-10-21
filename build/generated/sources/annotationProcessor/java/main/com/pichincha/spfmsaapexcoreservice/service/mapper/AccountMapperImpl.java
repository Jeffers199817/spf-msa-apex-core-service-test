package com.pichincha.spfmsaapexcoreservice.service.mapper;

import com.pichincha.spfmsaapexcoreservice.domain.Account;
import com.pichincha.spfmsaapexcoreservice.domain.Client;
import com.pichincha.spfmsaapexcoreservice.domain.enums.AccountType;
import com.pichincha.spfmsaapexcoreservice.model.AccountDTO;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public AccountDTO toDto(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setClientId( accountClientPersonId( account ) );
        accountDTO.setAccountId( account.getAccountId() );
        accountDTO.setAccountNumber( account.getAccountNumber() );
        accountDTO.setAccountType( accountTypeToAccountTypeEnum( account.getAccountType() ) );
        accountDTO.setInitialBalance( account.getInitialBalance() );
        accountDTO.setStatus( account.getStatus() );
        accountDTO.setClient( clientMapper.toDto( account.getClient() ) );

        return accountDTO;
    }

    @Override
    public Account toEntity(AccountDTO accountDTO) {
        if ( accountDTO == null ) {
            return null;
        }

        Account account = new Account();

        account.setClient( accountDTOToClient( accountDTO ) );
        account.setAccountId( accountDTO.getAccountId() );
        account.setAccountNumber( accountDTO.getAccountNumber() );
        account.setAccountType( accountTypeEnumToAccountType( accountDTO.getAccountType() ) );
        account.setInitialBalance( accountDTO.getInitialBalance() );
        account.setStatus( accountDTO.getStatus() );

        return account;
    }

    private Long accountClientPersonId(Account account) {
        Client client = account.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getPersonId();
    }

    protected AccountDTO.AccountTypeEnum accountTypeToAccountTypeEnum(AccountType accountType) {
        if ( accountType == null ) {
            return null;
        }

        AccountDTO.AccountTypeEnum accountTypeEnum;

        switch ( accountType ) {
            case SAVINGS: accountTypeEnum = AccountDTO.AccountTypeEnum.SAVINGS;
            break;
            case CHECKING: accountTypeEnum = AccountDTO.AccountTypeEnum.CHECKING;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + accountType );
        }

        return accountTypeEnum;
    }

    protected Client accountDTOToClient(AccountDTO accountDTO) {
        if ( accountDTO == null ) {
            return null;
        }

        Client client = new Client();

        client.setPersonId( accountDTO.getClientId() );

        return client;
    }

    protected AccountType accountTypeEnumToAccountType(AccountDTO.AccountTypeEnum accountTypeEnum) {
        if ( accountTypeEnum == null ) {
            return null;
        }

        AccountType accountType;

        switch ( accountTypeEnum ) {
            case SAVINGS: accountType = AccountType.SAVINGS;
            break;
            case CHECKING: accountType = AccountType.CHECKING;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + accountTypeEnum );
        }

        return accountType;
    }
}
