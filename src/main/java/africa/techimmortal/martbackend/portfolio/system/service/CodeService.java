package africa.techimmortal.martbackend.portfolio.system.service;

import africa.techimmortal.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.techimmortal.martbackend.portfolio.system.domain.model.Code;
import africa.techimmortal.martbackend.portfolio.system.domain.model.CodeValue;
import org.springframework.data.domain.Pageable;

public interface CodeService {
    BasePageableResponse<Code> retrieveAllCodes(Pageable pageable, String filterValue);

    BasePageableResponse<CodeValue> retrieveAllCodeValuesByCodeId(Long id, Pageable pageable);

    CodeValue retrieveAllCodeValuesByCodeNameAndCodeValueName(String codeName, String codeValueName);
}
