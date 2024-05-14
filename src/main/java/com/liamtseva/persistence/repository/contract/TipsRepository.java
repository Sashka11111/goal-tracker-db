package com.liamtseva.persistence.repository.contract;

import com.liamtseva.persistence.entity.Tips;
import com.liamtseva.persistence.entity.User;
import com.liamtseva.domain.exception.EntityNotFoundException;
import java.util.List;

public interface TipsRepository {
  List<Tips> getAllTips();

}