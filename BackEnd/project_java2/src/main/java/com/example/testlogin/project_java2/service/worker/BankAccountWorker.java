package com.example.testlogin.project_java2.service.worker;

import com.example.testlogin.project_java2.constant.EnumConstant;
import com.example.testlogin.project_java2.dto.BankAccountDto;
import com.example.testlogin.project_java2.mapper.BankAccountMapper;
import com.example.testlogin.project_java2.model.BankAccount;
import com.example.testlogin.project_java2.model.UserAccount;
import com.example.testlogin.project_java2.repo.BankAccountRepo;
import com.example.testlogin.project_java2.repo.UserRepo;
import com.example.testlogin.project_java2.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BankAccountWorker implements BankAccountService {


    private BankAccountRepo bankAccountRepo;
    private UserRepo userRepo;

    @Transactional
    public boolean increase_amount_bank_account(double amount, String code, BankAccount bankAccountUser){
        try{
            System.out.println("amount user input: " + amount);
            System.out.println("current amount in bank account " + bankAccountUser.getAmount());
            if(bankAccountUser.getAmount() == 0.0){
                bankAccountUser.setCode(code);
                bankAccountUser.setAmount(amount);
                Optional<BankAccount> bankAccountUpdated = bankAccountRepo.findById(bankAccountUser.getId());
                bankAccountUpdated.ifPresent(bankAccount -> System.out.println("Amount in bank account updated " + bankAccount.getAmount()));
                return true;
            }
            bankAccountUser.setCode(code);
            bankAccountRepo.updateAmountByUser(bankAccountUser.getId(), code, amount);

            return true;
        }catch (Exception exception){
            return false;
        }
    }


    @Override
    public BankAccountDto active_bank_account(UserAccount user) {

        Optional<UserAccount> optionalUser = userRepo.findById(user.getId());

        if (optionalUser.isPresent()) {

            UserAccount existingUser = optionalUser.get();
            System.out.println("User id: " + existingUser.getId());
            System.out.println("User active Bank account: " + existingUser.getEmail());

            BankAccountDto bankAccountDto = new BankAccountDto();
            BankAccount bankAccount = new BankAccount();

            BankAccount bankAccountByUser = bankAccountRepo.findByUserAccount(user);

            if(bankAccountByUser == null){
                bankAccount.setId(existingUser.getId());
                bankAccount.setAmount(0.0);
                bankAccount.setStatus(EnumConstant.isACTIVE);
                bankAccount.setUserAccount(existingUser);
                BankAccount newBankAccount = bankAccountRepo.save(bankAccount);

                bankAccountDto.setId(newBankAccount.getId());
                bankAccountDto.setAmount(newBankAccount.getAmount());
                bankAccountDto.setStatus(newBankAccount.getStatus());
                bankAccountDto.setUserAccount(existingUser);
                System.out.println("New BankAccount: " + bankAccountDto);

                return bankAccountDto;
            }

            bankAccountDto.setId(bankAccountByUser.getId());
            bankAccountDto.setAmount(bankAccountByUser.getAmount());
            bankAccountDto.setStatus(bankAccountByUser.getStatus());
            bankAccountDto.setUserAccount(existingUser);

            return bankAccountDto;
        }
        return  null;
    }

    @Override
    public List<BankAccountDto> listBankAccountApi() {

        List<BankAccount> bankAccountDtoList = bankAccountRepo.findAll();

        return bankAccountDtoList.stream().map(
                BankAccountMapper::mapToBankAccountApiDto
        ).collect(Collectors.toList());
    }

    @Override
    public BankAccountDto getBankAccountDtoByUser(UserAccount userAccount) {

        BankAccount bankAccount = bankAccountRepo.findByUserAccount(userAccount);
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setId(bankAccount.getId());
        bankAccountDto.setCode(bankAccount.getCode());
        bankAccountDto.setAmount(bankAccount.getAmount());
        bankAccountDto.setStatus(bankAccount.getStatus());
        bankAccountDto.setUploads(bankAccount.getUserAccount().getUploads());
        bankAccountDto.setPayments(bankAccount.getPayments());

        return bankAccountDto;
    }

    @Override
    public BankAccount findByUserAccount(UserAccount userAccount) {
        return bankAccountRepo.findByUserAccount(userAccount);
    }
}
