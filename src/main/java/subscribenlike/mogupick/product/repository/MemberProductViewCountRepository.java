package subscribenlike.mogupick.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import subscribenlike.mogupick.product.common.ProductErrorCode;
import subscribenlike.mogupick.product.common.ProductException;
import subscribenlike.mogupick.product.domain.MemberProductViewCount;
import subscribenlike.mogupick.product.domain.Product;
import subscribenlike.mogupick.product.model.query.FetchPeerBestReviewsQueryResult;

import java.util.List;
import java.util.Map;

public interface MemberProductViewCountRepository extends JpaRepository<MemberProductViewCount, Long>, MemberProductViewCountQuerydslRepository {

}
