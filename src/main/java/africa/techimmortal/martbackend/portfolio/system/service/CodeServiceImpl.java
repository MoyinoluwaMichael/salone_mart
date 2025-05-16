package africa.techimmortal.martbackend.portfolio.system.service;

import africa.techimmortal.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.techimmortal.martbackend.portfolio.system.domain.model.Code;
import africa.techimmortal.martbackend.portfolio.system.domain.model.CodeValue;
import africa.techimmortal.martbackend.portfolio.system.domain.repository.CodeRepository;
import africa.techimmortal.martbackend.portfolio.system.domain.repository.CodeValueRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final CodeRepository codeRepository;
    private final CodeValueRepository codeValueRepository;

    public static final String PRODUCT_CATEGORY = "Product Category";

    @Override
    public BasePageableResponse<Code> retrieveAllCodes(Pageable pageable, String filterValue) {
        Page<Code> codes = codeRepository.findAll(pageable);
        return BasePageableResponse.instance(codes);
    }

    @Override
    public BasePageableResponse<CodeValue> retrieveAllCodeValuesByCodeId(Long id, Pageable pageable) {
        Page<CodeValue> codeValues = codeValueRepository.findAllByCodeId(id, pageable);
        return BasePageableResponse.instance(codeValues);
    }

    @Override
    public CodeValue retrieveAllCodeValuesByCodeNameAndCodeValueName(String codeName, String codeValueName) {
        Code code = codeRepository.findByName(codeName)
                .orElseThrow(() -> new IllegalArgumentException("Code with name " + codeName + " not found"));
        return codeValueRepository.findByCodeIdAndName(code.getId(), codeValueName)
                .orElseThrow(() -> new IllegalArgumentException("Codevalue with name " + codeValueName + " not found"));
    }
}
