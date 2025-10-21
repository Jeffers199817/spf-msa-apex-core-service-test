package com.pichincha.spfmsaapexcoreservice.service.mapper;

import com.pichincha.spfmsaapexcoreservice.domain.Account;
import com.pichincha.spfmsaapexcoreservice.domain.Transaction;
import com.pichincha.spfmsaapexcoreservice.domain.enums.TransactionType;
import com.pichincha.spfmsaapexcoreservice.model.TransactionDTO;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public TransactionDTO toDto(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setAccountId( transactionAccountAccountId( transaction ) );
        transactionDTO.setTransactionId( transaction.getTransactionId() );
        transactionDTO.setDate( map( transaction.getDate() ) );
        transactionDTO.setTransactionType( transactionTypeToTransactionTypeEnum( transaction.getTransactionType() ) );
        transactionDTO.setAmount( transaction.getAmount() );
        transactionDTO.setBalance( transaction.getBalance() );
        transactionDTO.setAccount( accountMapper.toDto( transaction.getAccount() ) );

        return transactionDTO;
    }

    @Override
    public Transaction toEntity(TransactionDTO transactionDTO) {
        if ( transactionDTO == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        transaction.setAccount( transactionDTOToAccount( transactionDTO ) );
        transaction.setAmount( transactionDTO.getAmount() );
        transaction.setBalance( transactionDTO.getBalance() );
        transaction.setDate( map( transactionDTO.getDate() ) );
        transaction.setTransactionId( transactionDTO.getTransactionId() );
        transaction.setTransactionType( transactionTypeEnumToTransactionType( transactionDTO.getTransactionType() ) );

        return transaction;
    }

    private Long transactionAccountAccountId(Transaction transaction) {
        Account account = transaction.getAccount();
        if ( account == null ) {
            return null;
        }
        return account.getAccountId();
    }

    protected TransactionDTO.TransactionTypeEnum transactionTypeToTransactionTypeEnum(TransactionType transactionType) {
        if ( transactionType == null ) {
            return null;
        }

        TransactionDTO.TransactionTypeEnum transactionTypeEnum;

        switch ( transactionType ) {
            case DEPOSIT: transactionTypeEnum = TransactionDTO.TransactionTypeEnum.DEPOSIT;
            break;
            case WITHDRAWAL: transactionTypeEnum = TransactionDTO.TransactionTypeEnum.WITHDRAWAL;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionType );
        }

        return transactionTypeEnum;
    }

    protected Account transactionDTOToAccount(TransactionDTO transactionDTO) {
        if ( transactionDTO == null ) {
            return null;
        }

        Account account = new Account();

        account.setAccountId( transactionDTO.getAccountId() );

        return account;
    }

    protected TransactionType transactionTypeEnumToTransactionType(TransactionDTO.TransactionTypeEnum transactionTypeEnum) {
        if ( transactionTypeEnum == null ) {
            return null;
        }

        TransactionType transactionType;

        switch ( transactionTypeEnum ) {
            case WITHDRAWAL: transactionType = TransactionType.WITHDRAWAL;
            break;
            case DEPOSIT: transactionType = TransactionType.DEPOSIT;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionTypeEnum );
        }

        return transactionType;
    }
}
