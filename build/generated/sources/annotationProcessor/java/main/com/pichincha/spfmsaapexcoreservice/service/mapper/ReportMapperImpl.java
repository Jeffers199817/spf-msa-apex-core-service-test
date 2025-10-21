package com.pichincha.spfmsaapexcoreservice.service.mapper;

import com.pichincha.spfmsaapexcoreservice.domain.Account;
import com.pichincha.spfmsaapexcoreservice.domain.Transaction;
import com.pichincha.spfmsaapexcoreservice.domain.enums.AccountType;
import com.pichincha.spfmsaapexcoreservice.model.ReportDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ReportMapperImpl implements ReportMapper {

    @Override
    public ReportDTO toDto(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        ReportDTO reportDTO = new ReportDTO();

        if ( transaction.getDate() != null ) {
            reportDTO.setDate( transaction.getDate().toLocalDate() );
        }
        reportDTO.setAccountNumber( transactionAccountAccountNumber( transaction ) );
        AccountType accountType = transactionAccountAccountType( transaction );
        if ( accountType != null ) {
            reportDTO.setType( accountType.name() );
        }
        reportDTO.setMovement( transaction.getAmount() );
        reportDTO.setAvailableBalance( transaction.getBalance() );

        reportDTO.setClient( getClientName(transaction) );
        reportDTO.setInitialBalance( transaction.getAccount().getInitialBalance() );
        reportDTO.setStatus( transaction.getAccount().getStatus() );

        return reportDTO;
    }

    private String transactionAccountAccountNumber(Transaction transaction) {
        Account account = transaction.getAccount();
        if ( account == null ) {
            return null;
        }
        return account.getAccountNumber();
    }

    private AccountType transactionAccountAccountType(Transaction transaction) {
        Account account = transaction.getAccount();
        if ( account == null ) {
            return null;
        }
        return account.getAccountType();
    }
}
