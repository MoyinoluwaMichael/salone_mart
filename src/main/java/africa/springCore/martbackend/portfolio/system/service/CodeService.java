package africa.springCore.martbackend.portfolio.system.service;

import africa.springCore.martbackend.core.domain.dtos.response.BasePageableResponse;
import africa.springCore.martbackend.portfolio.system.domain.model.Code;
import africa.springCore.martbackend.portfolio.system.domain.model.CodeValue;
import org.springframework.data.domain.Pageable;

public interface CodeService {
    BasePageableResponse<Code> retrieveAllCodes(Pageable pageable, String filterValue);

    BasePageableResponse<CodeValue> retrieveAllCodeValuesByCodeId(Long id, Pageable pageable);

    CodeValue retrieveAllCodeValuesByCodeNameAndCodeValueName(String codeName, String codeValueName);
}
